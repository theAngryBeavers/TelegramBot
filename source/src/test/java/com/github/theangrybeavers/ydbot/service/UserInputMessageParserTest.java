package com.github.theangrybeavers.ydbot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.Arrays;

@TestInstance(Lifecycle.PER_CLASS)
public class UserInputMessageParserTest {
	private static String[] userInput;
	private static String[] expectedYouTubeVideoIds;
	private static String[] expectedOutputFileExtensions;

	private UserInputMessageParser parser;

	@BeforeAll
	public void init() {
		parser = new UserInputMessageParser();
		userInput = new String[] {
				"mp3         https://www.youtube-nocookie.com/embed/up_lNV-yoK4?rel=0",
				"Mp3  http://www.youtube.com/user/Scobleizer#p/u/1/1p3vcRhsYGo",
				"mP3  http://www.youtube.com/watch?v=cKZDdG9FTKY&feature=channel",
				"MP3 http://www.youtube.com/watch?v=yZ-K7nCVnBI&playnext_from=TL&videos=osPknwzXEas&feature=sub",
				"MP4           http://www.youtube.com/ytscreeningroom?v=NRHVzbJVx8I",
				"Mp4 http://www.youtube.com/user/SilkRoadTheatre#p/a/u/2/6dwqZw0j_jY",
				"mP4 http://youtu.be/6dwqZw0j_jY",
				"mp4 http://www.youtube.com/watch?v=6dwqZw0j_jY&feature=youtu.be",
				"mp3 http://youtu.be/afa-5HQHiAs",
		};

		expectedYouTubeVideoIds = new String[] {
				"up_lNV-yoK4",
				"1p3vcRhsYGo",
				"cKZDdG9FTKY",
				"yZ-K7nCVnBI",
				"NRHVzbJVx8I",
				"6dwqZw0j_jY",
				"6dwqZw0j_jY",
				"6dwqZw0j_jY",
				"afa-5HQHiAs",
		};

		expectedOutputFileExtensions = new String[] {
				"MP3",
				"MP3",
				"MP3",
				"MP3",
				"MP4",
				"MP4",
				"MP4",
				"MP4",
				"MP3"
		};

	}

	@Test
	public void getYouTubeVideoIdsWhenCallGetYouTubeVideoId() {
		Assertions.assertTrue(Arrays.deepEquals(expectedYouTubeVideoIds,
				Arrays.stream(userInput)
				.map(parser::getYouTubeVideoId)
				.toArray()));
	}

	@Test
	public void getOutputFileExtensionsWhenCallGetOutputFileExtension() {
		Assertions.assertTrue(Arrays.deepEquals(expectedOutputFileExtensions,
				Arrays.stream(userInput)
						.map(parser::getOutputFileExtension)
						.toArray()));
	}
}
