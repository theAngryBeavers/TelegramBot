package com.github.theangrybeavers.ydbot.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service("UserInputMessageParser")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserInputMessageParser {
	private static final String YOUTUBE_URL_PATTERN_V1 = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
	private static final String YOUTUBE_URL_PATTERN_V2 = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";
	private static final String OUTPUT_FILE_EXTENSION = "mp[34]";

	public String getYouTubeVideoId(final String message) {
		var youtubeUrl = message.split("\s+")[1];
		var youtubeUrlMatcherV1 = Pattern.compile(YOUTUBE_URL_PATTERN_V1,
				Pattern.CASE_INSENSITIVE).matcher(youtubeUrl);
		var youtubeUrlMatcherV2 = Pattern.compile(YOUTUBE_URL_PATTERN_V2,
				Pattern.CASE_INSENSITIVE).matcher(youtubeUrl);
		return youtubeUrlMatcherV1.find()
				? youtubeUrlMatcherV1.group()
				: youtubeUrlMatcherV2.find()
				? youtubeUrlMatcherV2.group(1)
				: null;
	}

	public String getOutputFileExtension(final String message) {
		var outputFileExtension = message.split("\s+")[0];
		var fileExtensionMatcher = Pattern.compile(OUTPUT_FILE_EXTENSION,
				Pattern.CASE_INSENSITIVE).matcher(outputFileExtension);
		return fileExtensionMatcher.find()
				? fileExtensionMatcher.group().toUpperCase()
				: null;
	}
}
