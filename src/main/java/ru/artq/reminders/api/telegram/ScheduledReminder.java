package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledReminder {
    private final TelegramBot telegramBot;
    private final ReminderService reminderService;
    private final UserService userService;
    private final UserSessionService userSessionService;

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        List<Long> usersId = userSessionService.getUserSessions()
                .values().stream().map(UserSession::getUserId).toList();
        if (usersId.isEmpty()) return;
        reminderService
                .findByRemindTimeBefore(LocalDateTime.now())
                .stream().filter(rem -> usersId.contains(rem.getUserId()))
                .forEach(rem -> {
                    String str = "Напоминание: " + rem.getTitle() + ", Описание: " + rem.getDescription();
                    long chatId = userService.findTelegramChatId(rem.getUserId());
                    telegramBot.sendMessage(chatId, str);
        });
    }
}
