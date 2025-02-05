package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.telegram.session.UserSessionService;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ScheduledReminder {
    private final TelegramBot telegramBot;
    private final ReminderService reminderService;
    private final UserSessionService userSessionService;

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        userSessionService.getUserSessions()
                .forEach((chatId, session) -> {
                    if (session.getUserId() == null) return;
                    reminderService.findActiveReminders(session.getUserId())
                            .forEach(reminder -> sendReminder(chatId, reminder));
                });
    }

    private void sendReminder(Long chatId, ReminderDto reminder) {
        String message = String.format(
                "⏰ Напоминание: %s\n📝 Описание: %s\n⏳ Время: %s",
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getRemind().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        );
        telegramBot.sendMessage(chatId, message);
        reminderService.markAsCompleted(reminder.getId());
    }
}
