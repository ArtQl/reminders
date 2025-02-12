package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.telegram.TelegramBot;
import ru.artq.reminders.api.telegram.session.UserSession;
import ru.artq.reminders.api.telegram.session.UserSessionService;
import ru.artq.reminders.api.telegram.session.UserStateType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CreateCommand implements Command {
    private final ReminderService reminderService;
    private final TelegramBot telegramBot;
    private final UserSessionService userSessionService;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        UserSession session = userSessionService.getUserSession(chatId);
        String text = update.getMessage().getText().trim();

        if (session.getState() == UserStateType.LOGGED) {
            telegramBot.sendMessage(chatId, "Введите заголовок напоминания:");
            session.setState(UserStateType.CREATE_REMINDER);
            session.setCommand("/new");
        } else if (session.getState() == UserStateType.CREATE_REMINDER) {
            handleCreateReminder(session, text, chatId);
        }
    }

    private void handleCreateReminder(UserSession session, String text, long chatId) {
        if (session.getTitle() == null) {
            session.setTitle(text);
            telegramBot.sendMessage(chatId, "Введите описание напоминания:");
        } else if (session.getDescription() == null) {
            session.setDescription(text);
            telegramBot.sendMessage(chatId, "Введите приоритет напоминания:");
        } else if (session.getPriority() == null) {
            session.setPriority(text);
            telegramBot.sendMessage(chatId, "Введите дату и время напоминания (в формате dd.MM.yyyy HH:mm):");
        } else {
            session.setDateTime(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            try {
                reminderService.createReminder(
                        session.getUserId(), session.getTitle(),
                        session.getDescription(), session.getPriority(),
                        session.getDateTime());
                telegramBot.sendMessage(chatId, "Напоминание добавлено.");
                session.setState(UserStateType.LOGGED);
                session.clear();
            } catch (Exception e) {
                telegramBot.sendMessage(chatId, "Ошибка! Неверный формат данных. Попробуйте снова.");
                session.setState(UserStateType.LOGGED);
            }
        }
    }
}
