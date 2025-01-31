package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.service.ReminderService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateReminderCommand implements Command {
    private final ReminderService reminderService;
    private final TelegramBot telegramBot;

    @Override
    public void execute(Update update) {
        if (!telegramBot.checkUserLogin(update)) return;

        String[] parts = update.getMessage().getText().split(" ");
        long chatId = update.getMessage().getChatId();
        if (parts.length <= 1 || parts.length > 5) {
            telegramBot.sendMessage(chatId, "Неверный формат. Используйте: /new [title] [description] [priority] [dateTime]");
            return;
        }
        try {
            String title = parts[1];
            String description = parts[2];
            String priority = parts[3];
            LocalDateTime dateTime = LocalDateTime.parse(parts[4]);
            reminderService.createReminder(telegramBot.getUser().getId(), title, description, priority, dateTime);
            telegramBot.sendMessage(chatId, "Напоминание добавлено.");
        } catch (Exception e) {
            telegramBot.sendMessage(chatId, e.getMessage());
        }
    }
}
