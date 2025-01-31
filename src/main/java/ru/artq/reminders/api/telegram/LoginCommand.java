package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.service.UserService;

@Component
@RequiredArgsConstructor
public class LoginCommand implements Command {
    private final TelegramBot telegramBot;
    private final UserService userService;

    @Override
    public void execute(Update update) {
        if (telegramBot.checkUserLogin(update)) return;
        long chatId = update.getMessage().getChatId();

        if (!userService.findUserByUsername(update.getMessage().getFrom().getUserName())) {
            telegramBot.sendMessage(chatId, "Вы не зарегистрированы в системе!");
            return;
        }

        String[] parts = update.getMessage().getText().split(" ");
        if (parts.length != 3) {
            telegramBot.sendMessage(chatId, "Неверный формат. Используйте: /login [email] [password]");
            return;
        }
        try {
            UserDto user = userService.findUserByEmailAndPassword(parts[1], parts[2]);
            telegramBot.setUser(user);
            telegramBot.sendMessage(chatId, "Вы успешно вошли в систему!");
        } catch (RuntimeException e) {
            telegramBot.sendMessage(chatId, e.getMessage());
        }
    }
}
