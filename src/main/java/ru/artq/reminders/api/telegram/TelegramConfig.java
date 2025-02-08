package ru.artq.reminders.api.telegram;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Configuration
public class TelegramConfig {

    @Bean
    public OkHttpTelegramClient confingOkHttpTelegramClient(@Value("${telegram.token}") String botToken) {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean
    public OkHttpClient configOkHttpClient() {
        return new OkHttpClient().newBuilder().build();
    }
}
