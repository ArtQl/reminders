package ru.artq.reminders.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.core.service.UserService;
import ru.artq.reminders.telegram.bot.ReminderHandler;
import ru.artq.reminders.telegram.session.UserSession;
import ru.artq.reminders.telegram.session.UserSessionService;
import ru.artq.reminders.telegram.session.UserStateType;

@Component
@RequiredArgsConstructor
public class RegistrationCommand implements Command {
    private final UserService userService;
    private final ReminderHandler reminderHandler;
    private final UserSessionService userSessionService;
    private final Environment env;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        UserSession session = userSessionService.getUserSession(chatId);

        if (userService.existsByUsername(update.getMessage().getFrom().getUserName())) {
            reminderHandler.sendMessage(chatId, env.getProperty("telegram.message.logged"));
            return;
        }

        if (session.getState() == UserStateType.START) {
            reminderHandler.sendMessage(chatId, "Введите ваш email:");
            session.setState(UserStateType.REGISTRATION);
            session.setCommand(env.getProperty("telegram.command.registration"));
        } else if (session.getState() == UserStateType.REGISTRATION) {
            handleRegistration(session, text, chatId, update.getMessage().getFrom().getUserName());
        }
    }

    private void handleRegistration(UserSession session, String text,
                                    long chatId, String username) {
        if (session.getEmail() == null) {
            session.setEmail(text);
            reminderHandler.sendMessage(chatId, "Введите ваш пароль:");
        } else if (session.getPassword() == null) {
            session.setPassword(text);
            try {
                UserDto user = userService.createUser(chatId, username,
                        session.getEmail(), session.getPassword());
                session.setUserId(user.getId());
                reminderHandler.sendMessage(chatId, env.getProperty("telegram.bot.login-message"));
                session.setState(UserStateType.LOGGED);
            } catch (RuntimeException e) {
                reminderHandler.sendMessage(chatId, e.getMessage());
                userSessionService.clearSession(chatId);
            }
        }
    }
}