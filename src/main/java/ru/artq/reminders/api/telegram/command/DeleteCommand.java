package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.api.telegram.TelegramBot;

@Component
@RequiredArgsConstructor
public class DeleteCommand implements Command {
    private final TelegramBot telegramBot;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();

        // todo: do delete command
        telegramBot.sendMessage(chatId, "Операция удаления временно не доступна!");
    }
}
