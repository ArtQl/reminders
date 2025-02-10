package ru.artq.reminders.telegram.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.core.exception.NotFoundException;
import ru.artq.reminders.core.service.ReminderService;
import ru.artq.reminders.telegram.bot.ReminderHandler;
import ru.artq.reminders.telegram.session.UserSession;
import ru.artq.reminders.telegram.session.UserSessionService;
import ru.artq.reminders.telegram.session.UserStateType;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteCommand implements Command {
    private final ReminderHandler reminderHandler;
    private final ReminderService reminderService;
    private final UserSessionService userSessionService;
    private final Environment env;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        UserSession session = userSessionService.getUserSession(chatId);

        if (findReminders(session, chatId)) return;

        if (session.getState() == UserStateType.LOGGED) {
            handleChooseReminder(session, chatId);
        } else if (session.getState() == UserStateType.DELETE) {
            handleDeleteReminder(update, session, chatId);
        }
    }

    private void handleDeleteReminder(Update update, UserSession session, long chatId) {
        try {
            int id = Integer.parseInt(update.getMessage().getText());
            reminderService.deleteReminder(session.getUserId(), session.getReminders().get(id - 1).getId());
            reminderHandler.sendMessage(chatId, "Напоминание удалено!");
            session.setState(UserStateType.LOGGED);
            Thread.sleep(2000);
            reminderHandler.sendMessage(chatId, env.getProperty("telegram.message.login"));
        } catch (NumberFormatException e) {
            String message = getMessage("Неправильный номер напоминания, попробуй снова:\n", session);
            reminderHandler.sendMessage(chatId, message);
        } catch (InterruptedException e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            Thread.currentThread().interrupt();
        }
    }

    private void handleChooseReminder(UserSession session, long chatId) {
        String message = getMessage("Выбери номер напоминания для удаления:\n", session);
        reminderHandler.sendMessage(chatId, message);
        session.setState(UserStateType.DELETE);
        session.setCommand(env.getProperty("telegram.command.delete"));
    }

    private static String getMessage(String str, UserSession session) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < session.getReminders().size(); i++) {
            sb.append("⏰ Напоминание №%d: %s\n".formatted(i + 1,
                    session.getReminders().get(i).getTitle()));
        }
        return sb.toString();
    }

    private boolean findReminders(UserSession session, long chatId) {
        try {
            session.setReminders(reminderService.findReminder(session.getUserId()));
        } catch (NotFoundException e) {
            reminderHandler.sendMessage(chatId, "Напоминания отсутствуют, операция удаления невозможна!");
            return true;
        }
        return false;
    }
}
