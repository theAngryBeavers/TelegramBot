package com.github.theangrybeavers.ydbot.controller;

import com.github.theangrybeavers.ydbot.core.YdBot;
import com.github.theangrybeavers.ydbot.model.YdBotInputError;
import com.github.theangrybeavers.ydbot.validator.YdBotUserInputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/")
public class WebHookController {
	private final YdBot ydBot;
	private final YdBotUserInputValidator ydBotUserInputValidator;

	@Autowired
	public WebHookController(final YdBot ydBot, final YdBotUserInputValidator ydBotUserInputValidator) {
		this.ydBot = ydBot;
		this.ydBotUserInputValidator = ydBotUserInputValidator;
	}

	@PostMapping
	public BotApiMethod onUpdateReceived(@RequestBody Update update) {
		if (update.getMessage() != null && update.getMessage().hasText()) {
			YdBotInputError inputError = ydBotUserInputValidator.validate(update);
			return inputError.hasError()
					? ydBot.onWebhookInputErrorReceived(inputError)
					: ydBot.onWebhookUpdateReceived(update);
		}
		return null;
	}
}
