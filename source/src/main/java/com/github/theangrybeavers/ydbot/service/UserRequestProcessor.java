package com.github.theangrybeavers.ydbot.service;

import com.github.theangrybeavers.ydbot.model.YdBotResponse;
import com.github.theangrybeavers.ydbot.model.YdBotResponseContentType;
import com.github.theangrybeavers.ydbot.service.mediaService.VideoToAudioConverter;
import com.github.theangrybeavers.ydbot.service.mediaService.YoutubeDownloader;
import com.sapher.youtubedl.YoutubeDLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ws.schild.jave.EncoderException;


@Service("UserRequestProcessor")
@PropertySource("classpath:/ydBotResponses.properties")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserRequestProcessor {
	private static final String ABSOLUTE_FILE_PATH = "C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia\\%s.%s";
	private static final String MP3 = "MP3";
	private static final String MP4 = "MP4";

	private final UserInputMessageParser messageParser;
	private final YoutubeDownloader youtubeDownloader;
	private final VideoToAudioConverter videoToAudioConverter;

	@Value("${response.incorrectVideo}")
	private String incorrectVideo;

	@Autowired
	public UserRequestProcessor(final UserInputMessageParser messageParser,
	                            final YoutubeDownloader youtubeDownloader,
	                            final VideoToAudioConverter videoToAudioConverter) {
		this.messageParser = messageParser;
		this.youtubeDownloader = youtubeDownloader;
		this.videoToAudioConverter = videoToAudioConverter;
	}

	public YdBotResponse processRequest(String message) {
		boolean isAudio = MP3.equals(messageParser.getOutputFileExtension(message));
		String videoId = messageParser.getYouTubeVideoId(message);
		String convertedAudio = String.format(ABSOLUTE_FILE_PATH, videoId, MP3);
		String downloadedVideo = String.format(ABSOLUTE_FILE_PATH, videoId, MP4);

		YdBotResponseContentType responseContentType = YdBotResponseContentType.MESSAGE;
		String contentPath = null;
		String outMessage = null;
		boolean containsMedia = false;

		try {
			youtubeDownloader.download(videoId);

			if (isAudio) {
				videoToAudioConverter.convert(downloadedVideo, convertedAudio);
			}

			responseContentType = isAudio
					? YdBotResponseContentType.MP3
					: YdBotResponseContentType.MP4;

			contentPath = isAudio
					? convertedAudio
					: downloadedVideo;

			containsMedia = true;
		} catch (YoutubeDLException | EncoderException ex) {
			outMessage = incorrectVideo + message.split("\s+")[1];
		}

		return new YdBotResponse(responseContentType,
				videoId,
				contentPath,
				outMessage,
				containsMedia);
	}
}
