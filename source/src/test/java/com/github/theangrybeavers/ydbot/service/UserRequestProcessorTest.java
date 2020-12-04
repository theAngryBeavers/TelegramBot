package com.github.theangrybeavers.ydbot.service;

import com.github.theangrybeavers.ydbot.model.YdBotResponse;
import com.github.theangrybeavers.ydbot.model.YdBotResponseContentType;
import com.github.theangrybeavers.ydbot.service.mediaService.VideoToAudioConverter;
import com.github.theangrybeavers.ydbot.service.mediaService.YoutubeDownloader;
import com.sapher.youtubedl.YoutubeDLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ws.schild.jave.EncoderException;

@TestInstance(Lifecycle.PER_CLASS)
public class UserRequestProcessorTest {

	private final static String FILE_EXTENSION = "MP3";
	private final static String CORRECT_USER_INPUT = "mp3 0123456789_";
	private final static String INVALID_USER_INPUT = "mp3 -----------";
	private final static String CORRECT_YOUTUBE_VIDEO_ID = "0123456789_";
	private final static String INVALID_YOUTUBE_VIDEO_ID = "-----------";

	@Mock
	private UserInputMessageParser parser;
	@Mock
	private YoutubeDownloader downloader;
	@Mock
	private VideoToAudioConverter converter;

	@InjectMocks
	private UserRequestProcessor processor;

	@BeforeAll
	public void init() throws YoutubeDLException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(parser.getOutputFileExtension(Mockito.anyString())).thenReturn(FILE_EXTENSION);
		Mockito.when(parser.getYouTubeVideoId(CORRECT_USER_INPUT)).thenReturn(CORRECT_YOUTUBE_VIDEO_ID);
		Mockito.when(parser.getYouTubeVideoId(INVALID_USER_INPUT)).thenReturn(INVALID_YOUTUBE_VIDEO_ID);
		Mockito.doThrow(YoutubeDLException.class).when(downloader).download(INVALID_YOUTUBE_VIDEO_ID);
	}

	@Test
	@Order(1)
	public void getYdBotResponseThatContainsMediaWhenCallProcess() throws YoutubeDLException, EncoderException {
		YdBotResponse expected = new YdBotResponse(YdBotResponseContentType.MP3,
				CORRECT_YOUTUBE_VIDEO_ID,
				"C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia\\0123456789_.MP3",
				null,
				true);
		YdBotResponse actual = processor.processRequest(CORRECT_USER_INPUT);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Order(2)
	public void getYdBotResponseThatHasNoMediaWhenCallProcess() throws YoutubeDLException, EncoderException {
		YdBotResponse expected = new YdBotResponse(YdBotResponseContentType.MESSAGE,
				INVALID_YOUTUBE_VIDEO_ID,
				null,
				null,
				false);
		YdBotResponse actual = processor.processRequest(INVALID_USER_INPUT);
		Assertions.assertEquals(expected, actual);
	}
}