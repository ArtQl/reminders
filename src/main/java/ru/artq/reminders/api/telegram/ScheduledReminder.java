package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.dto.ReminderDto;
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

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        List<ReminderDto> list = reminderService.findByRemindTimeBefore(LocalDateTime.now());
        list.forEach(rem -> {
            Long chatId = userService.findTelegramChatId(rem.getUserId());
            telegramBot.sendMessage(chatId, "Напоминание: " + rem.getTitle());
        });
    }
}
