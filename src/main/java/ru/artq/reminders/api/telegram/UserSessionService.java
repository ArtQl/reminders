package ru.artq.reminders.api.telegram;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class UserSessionService {
    private final Map<Long, UserSession> userSessions = new HashMap<>();

    public UserSession getUserSession(Long chatId) {
        return userSessions.computeIfAbsent(chatId, k -> new UserSession());
    }


    public void clearSession(Long chatId) {
        userSessions.remove(chatId);
    }
}
