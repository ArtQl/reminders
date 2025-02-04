package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.service.UserService;
import ru.artq.reminders.api.telegram.TelegramBot;
import ru.artq.reminders.api.telegram.UserSession;
import ru.artq.reminders.api.telegram.UserSessionService;
import ru.artq.reminders.api.telegram.UserStateType;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateReminderCommand implements Command {
    private final ReminderService reminderService;
    private final TelegramBot telegramBot;
    private final UserSessionService userSessionService;

    @Override
    public void execute(Update update) {
        if (telegramBot.checkUserNotLogin(update)) return;

        long chatId = update.getMessage().getChatId();
        UserSession session = userSessionService.getUserSession(chatId);
        String text = update.getMessage().getText();

        switch (session.getState()) {
            case START:
                telegramBot.sendMessage(chatId, "Введите заголовок напоминания:");
                session.setState(UserStateType.CREATE_REMINDER);
                break;
            case CREATE_REMINDER:
                if (session.getTitle() == null) {
                    session.setTitle(text);
                    telegramBot.sendMessage(chatId, "Введите описание напоминания:");
                } else if (session.getDescription() == null) {
                    session.setDescription(text);
                    telegramBot.sendMessage(chatId, "Введите приоритет напоминания:");
                } else if (session.getPriority() == null) {
                    session.setPriority(text);
                    telegramBot.sendMessage(chatId, "Введите дату и время напоминания (в формате yyyy-MM-ddTHH:mm):");
                } else {
                    try {
                        session.setDateTime(LocalDateTime.parse(text));
                        reminderService.createReminder(
                                session.getUserId(),
                                session.getTitle(),
                                session.getDescription(),
                                session.getPriority(),
                                session.getDateTime());
                        telegramBot.sendMessage(chatId, "Напоминание добавлено.");
                        userSessionService.clearSession(chatId);
                    } catch (Exception e) {
                        telegramBot.sendMessage(chatId, "Ошибка! Неверный формат даты и времени. Попробуйте снова.");
                        session.setPriority(null);
                    }
                }
                break;
        }
    }
}
