package com.github.theangrybeavers.ydbot.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.File;

@TestInstance(Lifecycle.PER_CLASS)
public class DownloadedMediaCleanerServiceTest {
	private final static String FILE_NAME = "Mzr8wrUizE4";
	private final static String FILES_FOLDER = "C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia";
	private final static String[] FILE_EXTENSIONS = {"MP3", "mP3", "Mp3", "mp3", "MP4", "mP4", "Mp4", "mp4"};


	private DownloadedMediaCleanerService cleaner;

	@BeforeAll
	public void init() {
		cleaner = new DownloadedMediaCleanerService();
	}

	@Test
	public void deleteAllFilesWithCertainNameWhenCallClean() {
		cleaner.clean(FILE_NAME);
		Assertions.assertEquals(0, FileUtils.listFiles(new File(FILES_FOLDER), FILE_EXTENSIONS, true)
				.stream()
				.filter(file -> FILE_NAME.equals(file.getName().replaceFirst("[.][^.]+$", "")))
				.count());

	}
}
