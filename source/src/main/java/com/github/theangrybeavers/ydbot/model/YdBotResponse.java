package com.github.theangrybeavers.ydbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Setter
@AllArgsConstructor
public class YdBotResponse {
	@Getter
	private YdBotResponseContentType responseContentType;
	@Getter
	private String name;
	private String contentPath;
	@Getter
	private String message;
	private boolean containsMediaData;

	public boolean isContainsMediaData() {
		return containsMediaData;
	}

	public InputStream getContentStream() {
		try {
			return containsMediaData
					? new FileInputStream(contentPath)
					: null;
		} catch (FileNotFoundException ex) {
			return null;
		}
	}
}
