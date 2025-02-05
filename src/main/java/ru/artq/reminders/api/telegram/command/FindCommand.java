package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.telegram.TelegramBot;
import ru.artq.reminders.api.telegram.session.UserSessionService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCommand implements Command {
    private final ReminderService reminderService;
    private final TelegramBot telegramBot;
    private final UserSessionService userSessionService;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder("Список ваших напоминаний:\n");

        try {
            List<ReminderDto> reminders = reminderService
                    .findReminder(userSessionService.getUserSession(chatId).getUserId());
            if (reminders.isEmpty()) {
                sb.append("Напоминаний не найдено.");
            } else {
                reminders.forEach(reminder -> {
                    String message = String.format(
                            "⏰ Напоминание: %s\n📝 Описание: %s\n⏳ Время: %s\n\uD83D\uDD25 Приоритет: %s \n\uD83C\uDFC1 Выполнено: %s",
                            reminder.getTitle(),
                            reminder.getDescription(),
                            reminder.getRemind().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                            reminder.getPriority(),
                            reminder.isCompleted() ? "✅" : "❌"
                    );
                    sb.append(message).append("\n\n");
                });
            }
            telegramBot.sendMessage(chatId, sb.toString());
        } catch (Exception e) {
            telegramBot.sendMessage(chatId, "Ошибка получения напоминаний: %s".formatted(e));
        }
    }
}
