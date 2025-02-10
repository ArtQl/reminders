package ru.artq.reminders.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.core.service.ReminderService;
import ru.artq.reminders.telegram.bot.ReminderHandler;
import ru.artq.reminders.telegram.session.UserSession;
import ru.artq.reminders.telegram.session.UserSessionService;
import ru.artq.reminders.telegram.session.UserStateType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CreateCommand implements Command {
    private final ReminderService reminderService;
    private final ReminderHandler reminderHandler;
    private final UserSessionService userSessionService;
    private final Environment env;

    @Override
    public void execute(Update update) {
        long chatId = update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
        String text = update.hasCallbackQuery()
                ? update.getCallbackQuery().getData()
                : update.getMessage().getText().trim();
        UserSession session = userSessionService.getUserSession(chatId);

        if (session.getState() == UserStateType.LOGGED) {
            reminderHandler.sendMessage(chatId, "Введите заголовок напоминания:");
            session.setState(UserStateType.CREATE_REMINDER);
            session.setCommand(env.getProperty("telegram.command.create"));
        } else if (session.getState() == UserStateType.CREATE_REMINDER) {
            handleCreateReminder(session, text, chatId);
        }
    }

    private void handleCreateReminder(UserSession session, String text, long chatId) {
        if (session.getTitle() == null) {
            session.setTitle(text);
            reminderHandler.sendMessage(chatId, "Введите описание напоминания:");
        } else if (session.getDescription() == null) {
            session.setDescription(text);
            reminderHandler.sendMessageWithKeyboard(chatId, "Выберите приоритет напоминания:");
        } else if (session.getPriority() == null) {
            session.setPriority(text);
            reminderHandler.sendMessage(chatId, "Введите дату и время напоминания (в формате 25.02.2025 18:25):");
        } else {
            try {
                session.setDateTime(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                reminderService.createReminder(
                        session.getUserId(), session.getTitle(),
                        session.getDescription(), session.getPriority(),
                        session.getDateTime());
                reminderHandler.sendMessage(chatId, "Напоминание добавлено.");
                session.setState(UserStateType.LOGGED);
                session.clear();
            } catch (Exception e) {
                reminderHandler.sendMessage(chatId, "Ошибка создания напоминания: %s".formatted(e));
                session.setState(UserStateType.LOGGED);
                session.clear();
            }
        }
    }
}
