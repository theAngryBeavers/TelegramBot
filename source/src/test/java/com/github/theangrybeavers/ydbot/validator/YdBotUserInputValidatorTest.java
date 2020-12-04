package com.github.theangrybeavers.ydbot.validator;

import com.github.theangrybeavers.ydbot.model.YdBotInputError;
import com.github.theangrybeavers.ydbot.service.UserInputMessageParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@TestInstance(Lifecycle.PER_CLASS)
public class YdBotUserInputValidatorTest {

	private final static String CORRECT_TEXT = "mP4   https://www.youtube.com/watch?v=Tew5ZCvkXf4";
	private final static String INVALID_TEXT = "mp6 - _https://www.google.com/watch?v=Tew5ZCvkXf4";

	@Mock
	private Message correctMessage;
	@Mock
	private Message invalidMessage;
	@Mock
	private Update correctUpdate;
	@Mock
	private Update invalidUpdate;

	private YdBotUserInputValidator validator;

	@BeforeAll
	public void init() {
		MockitoAnnotations.initMocks(this);
		validator = new YdBotUserInputValidator(new UserInputMessageParser());
		Mockito.when(correctMessage.getText()).thenReturn(CORRECT_TEXT);
		Mockito.when(invalidMessage.getText()).thenReturn(INVALID_TEXT);
		Mockito.when(correctUpdate.getMessage()).thenReturn(correctMessage);
		Mockito.when(invalidUpdate.getMessage()).thenReturn(invalidMessage);
	}

	@Test
	public void validationSucceedWhenCallValidate() {
		YdBotInputError error = validator.validate(correctUpdate);
		Assertions.assertFalse(error.hasError());
	}

	@Test
	public void validationFailedWhenCallValidate() {
		YdBotInputError error = validator.validate(invalidUpdate);
		Assertions.assertTrue(error.hasError());
	}
}
