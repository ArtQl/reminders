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
public class RegistrationCommand implements Command {
    private final UserService userService;
    private final TelegramBot telegramBot;
    private final UserSessionService userSessionService;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        UserSession session = userSessionService.getUserSession(chatId);

        if (telegramBot.checkUserLogin(update)) return;
        String text = update.getMessage().getText();

        if (session.getState() == UserStateType.START) {
            telegramBot.sendMessage(chatId, "Введите ваш email:");
            session.setState(UserStateType.REGISTRATION);
            session.setCommand("/registration");
        } else if (session.getState() == UserStateType.REGISTRATION) {
            registration(session, text, chatId, update.getMessage().getFrom().getUserName());
        }
    }

    private void registration(UserSession session, String text, long chatId, String username) {
        if (session.getEmail() == null) {
            session.setEmail(text);
            telegramBot.sendMessage(chatId, "Введите ваш пароль:");
        } else if (session.getPassword() == null) {
            session.setPassword(text);
        } else {
            try {
                UserDto userDto = userService.createUser(chatId, username, session.getEmail(), session.getPassword());
                telegramBot.setUser(userDto);
                telegramBot.sendMessage(chatId, "Вы успешно вошли в систему!");
                userSessionService.clearSession(chatId);
            } catch (RuntimeException e) {
                telegramBot.sendMessage(chatId, e.getMessage());
                session.setPriority(null);
            }
        }
    }

}
