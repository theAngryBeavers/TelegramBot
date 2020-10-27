package com.github.theangrybeavers.ydbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class YdBotInputError {
	private final boolean containsError;
	@Getter
	private final long chatId;
	@Getter
	private final String errorMessage;

	public boolean hasError() {
		return containsError;
	}
}
