package ru.artq.reminders.api.telegram;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
    @Bean
    public UserSessionService userSessionService() {
        return new UserSessionService();
    }
}
