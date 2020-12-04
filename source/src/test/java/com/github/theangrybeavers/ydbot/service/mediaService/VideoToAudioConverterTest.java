package com.github.theangrybeavers.ydbot.service.mediaService;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;

@TestInstance(Lifecycle.PER_CLASS)
public class VideoToAudioConverterTest {
	private final static String MP4_FILE_IN = "C:\\Users\\pinky\\Desktop\\YD-bot\\src\\test\\resources\\filesToCompare\\Mzr8wrUizE4.mp4";
	private final static String MP3_FILE_OUT = "C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia\\Mzr8wrUizE4.mp3";
	private VideoToAudioConverter converter;

	@BeforeAll
	public void init() {
		converter = new VideoToAudioConverter();
	}

	@Test
	public void convertVideoToAudioWhenCallConvert() throws EncoderException, IOException {
		converter.convert(MP4_FILE_IN, MP3_FILE_OUT);
		File expected = new File("C:\\Users\\pinky\\Desktop\\YD-bot\\src\\test\\resources\\filesToCompare\\Mzr8wrUizE4.mp3");
		File actual = new File(MP3_FILE_OUT);
		Assertions.assertTrue(FileUtils.contentEquals(expected, actual));
	}

}
