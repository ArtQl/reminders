package ru.artq.reminders.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artq.reminders.telegram.bot.ReminderHandler;

@Component
@RequiredArgsConstructor
public class UpdateCommand implements Command {
    private final ReminderHandler reminderHandler;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();

        // todo: do update command
        reminderHandler.sendMessage(chatId, "Операция обновления временно не доступна!");
    }
}
