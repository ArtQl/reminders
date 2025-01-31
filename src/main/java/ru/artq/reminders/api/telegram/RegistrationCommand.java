package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.service.UserService;

@Component
@RequiredArgsConstructor
public class RegistrationCommand implements Command {
    private final UserService userService;
    private final TelegramBot telegramBot;

    @Override
    public void execute(Update update) {
        if (telegramBot.checkUserLogin(update)) return;

        String[] parts = update.getMessage().getText().split(" ");
        long chatId = update.getMessage().getChatId();
        if (parts.length != 3) {
            telegramBot.sendMessage(chatId, "Неверный формат. Используйте: /login [email] [password]");
            return;
        }
        try {
            UserDto userDto = userService.createUser(chatId, update.getMessage().getFrom().getUserName(), parts[1], parts[2]);
            telegramBot.setUser(userDto);
            telegramBot.sendMessage(chatId, "Вы успешно вошли в систему!");
        } catch (RuntimeException e) {
            telegramBot.sendMessage(chatId, e.getMessage());
        }
    }
}
