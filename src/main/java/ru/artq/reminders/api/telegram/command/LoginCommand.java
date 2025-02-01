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
        UserSession userSession = userSessionService.getUserSession(chatId);

        if (telegramBot.checkUserLogin(update)) return;
        if (!userService.findUserByUsername(update.getMessage().getFrom().getUserName())) {
            telegramBot.sendMessage(chatId, "Вы не зарегистрированы в системе!");
            return;
        }

        if (userSession.getState() == UserStateType.START) {
            telegramBot.sendMessage(chatId, "Введите ваш email:");
            userSession.setState(UserStateType.LOGIN);
            userSession.setCommand("/login");
        } else if (userSession.getState() == UserStateType.LOGIN) {
            if (userSession.getEmail() == null) {
                userSession.setEmail(text);
                telegramBot.sendMessage(chatId, "Введите ваш пароль:");
            } else if (userSession.getPassword() == null) {
                try {
                    userSession.setPassword(text);
                    UserDto user = userService.findUserByEmailAndPassword(
                            userSession.getEmail(), userSession.getPassword());
                    telegramBot.setUser(user);
                    telegramBot.sendMessage(chatId, "Вы успешно вошли в систему!");
                } catch (RuntimeException e) {
                    telegramBot.sendMessage(chatId, e.getMessage());
                }
            }
        }
    }
}
