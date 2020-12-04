package com.github.theangrybeavers.ydbot.service.mediaService;

import com.sapher.youtubedl.YoutubeDLException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.io.File;
import java.io.IOException;

@TestInstance(Lifecycle.PER_CLASS)
public class YoutubeDownloaderTest {
	private final static String CORRECT_VIDEO_ID = "Mzr8wrUizE4";
	private final static String INVALID_VIDEO_ID = "0-------1--";

	private YoutubeDownloader youtubeDownloader;

	@BeforeAll
	public void init() {
		youtubeDownloader = new YoutubeDownloader();
	}

	@Test
	public void downloadVideoWhenCallDownload() throws YoutubeDLException, IOException {
		youtubeDownloader.download(CORRECT_VIDEO_ID);
		File expected = new File("C:\\Users\\pinky\\Desktop\\YD-bot\\src\\test\\resources\\filesToCompare\\Mzr8wrUizE4.mp4");
		File actual = new File("C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia\\Mzr8wrUizE4.mp4");
		Assertions.assertTrue(FileUtils.contentEquals(expected, actual));
	}

	@Test
	public void getYoutubeDLExceptionWhenCallDownload() {
		Assertions.assertThrows(YoutubeDLException.class,
				() -> youtubeDownloader.download(INVALID_VIDEO_ID));
	}
}
