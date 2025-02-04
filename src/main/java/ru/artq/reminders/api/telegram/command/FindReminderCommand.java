package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.telegram.TelegramBot;
import ru.artq.reminders.api.telegram.UserSessionService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindReminderCommand implements Command {
    private final ReminderService reminderService;
    private final TelegramBot telegramBot;
    private final UserSessionService userSessionService;

    @Override
    public void execute(Update update) {
        if (telegramBot.checkUserNotLogin(update)) return;
        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder("Список ваших напоминаний:\n");
        try {
            List<ReminderDto> list = reminderService.findReminder(userSessionService.getUserSession(chatId).getUserId());
            list.forEach(rem -> {
                sb.append(rem);
                sb.append("\n");
            });
            telegramBot.sendMessage(chatId, sb.toString());
        } catch (Exception e) {
            telegramBot.sendMessage(chatId, e.getMessage());
        }
    }
}
