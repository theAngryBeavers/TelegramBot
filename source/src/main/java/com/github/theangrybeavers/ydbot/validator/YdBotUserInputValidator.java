package com.github.theangrybeavers.ydbot.validator;

import com.github.theangrybeavers.ydbot.model.YdBotInputError;
import com.github.theangrybeavers.ydbot.service.UserInputMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service("YdBotUserInputValidator")
@PropertySource("classpath:/ydBotResponses.properties")
public class YdBotUserInputValidator {
	private final static String START_COMMAND = "/start";

	private final UserInputMessageParser messageParser;
	@Value("${response.invalidInput}")
	private String invalidInputMessage;

	@Autowired
	public YdBotUserInputValidator(final UserInputMessageParser messageParser) {
		this.messageParser = messageParser;
	}

	public YdBotInputError validate(Update update) {
		String messageText = update.getMessage().getText();
		long chatId = update.getMessage().getChatId();
		return !(messageText.split("\s+").length == 1
				&& START_COMMAND.equals(messageText))
				&& (messageText.split("\s+").length != 2
				|| messageParser.getOutputFileExtension(messageText) == null
				|| messageParser.getYouTubeVideoId(messageText) == null)
				? new YdBotInputError(true, chatId, invalidInputMessage)
				: new YdBotInputError(false, chatId, null);
	}
}
