package com.github.theangrybeavers.ydbot.appconfig;

import com.github.theangrybeavers.ydbot.core.YdBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class YdBotConfig {
	private String botUsername;
	private String botToken;
	private String botPath;

	@Bean
	public YdBot getConfiguredYdTelegramBot() {
		YdBot instance = new YdBot();

		instance.setBotUsername(botUsername);
		instance.setBotToken(botToken);
		instance.setBotPath(botPath);

		return instance;
	}
}
