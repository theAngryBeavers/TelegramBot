package com.github.theangrybeavers.ydbot.core;

import com.github.theangrybeavers.ydbot.dao.YdBotUploadedFilesDao;
import com.github.theangrybeavers.ydbot.dao.YdBotUsersDao;
import com.github.theangrybeavers.ydbot.model.YdBotInputError;
import com.github.theangrybeavers.ydbot.model.YdBotResponse;
import com.github.theangrybeavers.ydbot.model.YdBotResponseContentType;
import com.github.theangrybeavers.ydbot.service.DownloadedMediaCleanerService;
import com.github.theangrybeavers.ydbot.service.UserInputMessageParser;
import com.github.theangrybeavers.ydbot.service.UserRequestProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Setter
@Getter
@PropertySource("classpath:/ydBotResponses.properties")
public class YdBot extends TelegramWebhookBot {
	private String botPath;
	private String botUsername;
	private String botToken;

	@Autowired
	private YdBotUsersDao usersDao;
	@Autowired
	private UserInputMessageParser messageParser;
	@Autowired
	private UserRequestProcessor requestProcessor;
	@Autowired
	private YdBotUploadedFilesDao uploadedFilesDao;
	@Autowired
	private DownloadedMediaCleanerService mediaCleanerService;

	@Value("${response.info}")
	private String infoMessage;

	@Value("${response.waitUntilDownloadingComplete}")
	private String waitUntilDownloadingCompleteMessage;

	@Value("${response.prepareFile}")
	private String prepareFileMessage;

	@Value("${response.incorrectVideo}")
	private String incorrectVideoMessage;

	@Override
	public BotApiMethod onWebhookUpdateReceived(Update update) {
		if (update.getMessage() != null && update.getMessage().hasText()) {
			processWebhookUpdate(update.getMessage().getChatId(),
					update.getMessage().getText());
		}
		return null;
	}

	private void processWebhookUpdate(final long chatId, final String message) {
		Integer botState = usersDao.getBotState(chatId);
		try {
			if (botState == null) {
				botIsInitialized(chatId, false);
			} else if (botState == 1 && message.equals("/start")) {
				botIsInitialized(chatId, true);
			} else if (botState == YdBotState.BUSY.ordinal()) {
				botIsBusy(chatId);
			} else if (botState == YdBotState.READY.ordinal()) {
				botIsReady(chatId, message);
			}
		} catch (TelegramApiException ex) {
			ex.printStackTrace();
		}
	}

	private void botIsInitialized(final long chatId, final boolean isAlreadyRegister) throws TelegramApiException {
		if (!isAlreadyRegister) {
			usersDao.putUser(chatId, YdBotState.READY.ordinal());
		}
		execute(new SendMessage(chatId, infoMessage));
	}

	private void botIsBusy(final long chatId) throws TelegramApiException {
		execute(new SendMessage(chatId, waitUntilDownloadingCompleteMessage));
	}

	private void botIsReady(final long chatId, final String message) throws TelegramApiException {

		usersDao.putUser(chatId, YdBotState.BUSY.ordinal());
		execute(new SendMessage(chatId, prepareFileMessage));

		String videoId = messageParser.getYouTubeVideoId(message);
		YdBotResponseContentType contentType = YdBotResponseContentType.valueOf(messageParser.getOutputFileExtension(message));
		String telegramFileId = uploadedFilesDao.getUploadedFile(videoId, contentType.ordinal());

		if (telegramFileId != null) {
			if (contentType == YdBotResponseContentType.MP3) {
				SendAudio audio = new SendAudio();
				audio.setChatId(chatId);
				audio.setAudio(telegramFileId);
				execute(audio);
			} else {
				SendVideo video = new SendVideo();
				video.setChatId(chatId);
				video.setVideo(telegramFileId);
				execute(video);
			}
		} else {

			YdBotResponse response = requestProcessor.processRequest(message);

			if (response.isContainsMediaData()) {
				if (response.getResponseContentType() == YdBotResponseContentType.MP3) {
					SendAudio audio = new SendAudio();
					audio.setAudio(response.getName(), response.getContentStream());
					audio.setChatId(chatId);

					telegramFileId = execute(audio).getAudio().getFileId();
				} else {
					SendVideo video = new SendVideo();
					video.setVideo(response.getName(), response.getContentStream());
					video.setChatId(chatId);

					telegramFileId = execute(video).getVideo().getFileId();
				}

				uploadedFilesDao.putFile(response.getName(),
						telegramFileId,
						response.getResponseContentType().ordinal());

				mediaCleanerService.clean(response.getName());
			} else {
				execute(new SendMessage(chatId, response.getMessage()));
			}
		}
		usersDao.putUser(chatId, YdBotState.READY.ordinal());
	}

	public BotApiMethod onWebhookInputErrorReceived(YdBotInputError inputError) {
		try {
			execute(new SendMessage(inputError.getChatId(), inputError.getErrorMessage()));
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		return null;
	}
}
