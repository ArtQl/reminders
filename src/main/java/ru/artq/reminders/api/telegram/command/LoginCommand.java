package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.service.UserService;
import ru.artq.reminders.api.telegram.TelegramBot;
import ru.artq.reminders.api.telegram.UserSession;
import ru.artq.reminders.api.telegram.UserSessionService;
import ru.artq.reminders.api.telegram.UserStateType;

@Component
@RequiredArgsConstructor
public class LoginCommand implements Command {
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final UserSessionService userSessionService;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        UserSession session = userSessionService.getUserSession(chatId);

        if (telegramBot.checkUserLogin(update)) return;

        if (!userService.existsByUsername(update.getMessage().getFrom().getUserName())) {
            telegramBot.sendMessage(chatId, "Вы не зарегистрированы в системе!");
            return;
        }

        if (session.getState() == UserStateType.START) {
            telegramBot.sendMessage(chatId, "Введите ваш email:");
            session.setState(UserStateType.LOGIN);
            session.setCommand("/login");
        } else if (session.getState() == UserStateType.LOGIN) {
            loginHandle(session, text, chatId);
        }
    }

    private void loginHandle(UserSession session,
                             String text, long chatId) {
        if (session.getEmail() == null) {
            session.setEmail(text);
            telegramBot.sendMessage(chatId, "Введите ваш пароль:");
        } else if (session.getPassword() == null) {
            session.setPassword(text);
            try {
                UserDto user = userService.findUserByEmailAndPassword(
                        session.getEmail(), session.getPassword());
                session.setUserId(user.getId());
                telegramBot.sendMessage(chatId, "Вы успешно вошли в систему!");
                session.setState(UserStateType.LOGGED);
            } catch (RuntimeException e) {
                telegramBot.sendMessage(chatId, e.getMessage());
                userSessionService.clearSession(chatId);
            }
        }
    }
}
