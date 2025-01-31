package ru.artq.reminders.api.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artq.reminders.api.dto.UserDto;

@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient = new OkHttpTelegramClient(getBotToken());
    private final CommandFactory commandFactory;
    private UserDto user;

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText().split(" ")[0];
            commandFactory.getCommand(command)
                    .ifPresentOrElse(
                            cmd -> cmd.execute(update),
                            () -> sendMessage(update.getMessage().getChatId(), MessagesTelegram.START_MESSAGE)
                    );
        }
    }

    @Override
    public String getBotToken() {
        return "7783739105:AAEqK4xTNb1aIv7BjeGLNtnCZ7mcZUJE9LM";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    public Boolean checkUserLogin(Update update) {
        if (user != null) {
            sendMessage(update.getMessage().getChatId(), MessagesTelegram.LOGIN_MESSAGE);
            return true;
        } else {
            sendMessage(update.getMessage().getChatId(), MessagesTelegram.NO_LOGIN_MESSAGE);
            return false;
        }
    }

    public void sendMessage(Long chat_id, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chat_id).text(text).build());
        } catch (TelegramApiException e) {
            log.warn("Telegram error: {} ", e.getMessage());
        }
    }
}
