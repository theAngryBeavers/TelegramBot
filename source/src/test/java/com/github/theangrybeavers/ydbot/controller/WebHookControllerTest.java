package com.github.theangrybeavers.ydbot.controller;

import com.github.theangrybeavers.ydbot.core.YdBot;
import com.github.theangrybeavers.ydbot.model.YdBotInputError;
import com.github.theangrybeavers.ydbot.validator.YdBotUserInputValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@TestInstance(Lifecycle.PER_CLASS)
public class WebHookControllerTest {

	private final static BotApiMethod correctMethod = new SendMessage();
	private final static BotApiMethod invalidMethod = new SendMessage();

	@Mock
	private YdBot ydBot;
	@Mock
	private Message message;
	@Mock
	private Update correctUpdate;
	@Mock
	private Update invalidUpdate;
	@Mock
	private YdBotUserInputValidator validator;
	@Mock
	private YdBotInputError inputErrorWithError;
	@Mock
	private YdBotInputError inputErrorWithoutError;

	@InjectMocks
	private WebHookController controller;

	@BeforeAll
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(message.hasText()).thenReturn(true);
		Mockito.when(correctUpdate.getMessage()).thenReturn(message);
		Mockito.when(invalidUpdate.getMessage()).thenReturn(message);
		Mockito.when(validator.validate(correctUpdate)).thenReturn(inputErrorWithoutError);
		Mockito.when(validator.validate(invalidUpdate)).thenReturn(inputErrorWithError);
		Mockito.when(ydBot.onWebhookInputErrorReceived(inputErrorWithError)).thenReturn(invalidMethod);
		Mockito.when(ydBot.onWebhookUpdateReceived(correctUpdate)).thenReturn(correctMethod);
		Mockito.when(inputErrorWithError.hasError()).thenReturn(true);
		Mockito.when(inputErrorWithoutError.hasError()).thenReturn(false);
	}

	@Test
	public void getEmptyUserInputErrorWhenCallOnUpdateReceived() {
		Assertions.assertSame(correctMethod, controller.onUpdateReceived(correctUpdate));
	}

	@Test
	public void getNonEmptyUserInputErrorWhenCallOnUpdateReceived() {
		Assertions.assertSame(invalidMethod, controller.onUpdateReceived(invalidUpdate));
	}
}
