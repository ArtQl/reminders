package ru.artq.reminders.telegram.session;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class UserSessionService {
    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    public UserSession getUserSession(Long chatId) {
        return userSessions.computeIfAbsent(chatId, k -> new UserSession());
    }

    public void clearSession(Long chatId) {
        userSessions.remove(chatId);
    }
}
