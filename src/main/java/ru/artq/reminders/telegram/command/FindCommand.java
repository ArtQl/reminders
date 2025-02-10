package ru.artq.reminders.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.core.service.ReminderService;
import ru.artq.reminders.telegram.bot.ReminderHandler;
import ru.artq.reminders.telegram.session.UserSessionService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCommand implements Command {
    private final ReminderService reminderService;
    private final ReminderHandler reminderHandler;
    private final UserSessionService userSessionService;
    @Value("${telegram.message.reminder}")
    private String reminderMessage;

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
                    String message = reminderMessage.formatted(
                            reminder.getTitle(),
                            reminder.getDescription(),
                            reminder.getRemind().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                            reminder.getPriority(),
                            reminder.isCompleted() ? "✅" : "❌"
                    );
                    sb.append(message).append("\n\n");
                });
            }
            reminderHandler.sendMessage(chatId, sb.toString());
        } catch (Exception e) {
            reminderHandler.sendMessage(chatId, "Ошибка получения напоминаний: %s".formatted(e));
        }
    }
}
