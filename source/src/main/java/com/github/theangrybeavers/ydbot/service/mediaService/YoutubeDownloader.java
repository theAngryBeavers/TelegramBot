package com.github.theangrybeavers.ydbot.service.mediaService;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("YoutubeDownloader")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class YoutubeDownloader {
	private final static String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
	private final static String DEFAULT_DIRECTORY = "C:\\Users\\pinky\\Desktop\\YD-bot\\src\\main\\resources\\downloadedMedia\\";

	public void download(final String youtubeVideoId) throws YoutubeDLException {
		var request = new YoutubeDLRequest(String.format(YOUTUBE_VIDEO_URL, youtubeVideoId), DEFAULT_DIRECTORY);
		request.setOption("format", "best");            // --format best
		request.setOption("ignore-errors");             // --ignore-errors
		request.setOption("output", "%(id)s.%(ext)s");  // --output "%(id)s.%(ext)s"
		request.setOption("retries", 10);               // --retries 10

		YoutubeDL.execute(request);
	}
}

