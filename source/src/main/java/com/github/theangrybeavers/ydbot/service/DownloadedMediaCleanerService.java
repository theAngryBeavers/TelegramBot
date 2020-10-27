package com.github.theangrybeavers.ydbot.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("DownloadedMediaCleanerService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DownloadedMediaCleanerService {

	private final static String MEDIA_FOLDER = "C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia";
	private final static String[] FILE_EXTENSIONS = {"MP3", "mP3", "Mp3", "mp3", "MP4", "mP4", "Mp4", "mp4"};

	public void clean(final String fileName) {
		FileUtils.listFiles(new File(MEDIA_FOLDER), FILE_EXTENSIONS, true).stream()
				.filter(file -> fileName.equals(file.getName().replaceFirst("[.][^.]+$", "")))
				.forEach(File::delete);

	}
}
