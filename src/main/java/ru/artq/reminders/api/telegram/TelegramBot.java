package ru.artq.reminders.api.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    @Value("${telegram.bot.token}")
    private String botToken;
    private final TelegramClient telegramClient = new OkHttpTelegramClient(getBotToken());
    private final CommandFactory commandFactory;
    private final UserSessionService userSessionService;

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            UserSession session = userSessionService.getUserSession(chatId);
            String command = update.getMessage().getText().split(" ")[0];

            if (session.getState() == UserStateType.START ||
                    session.getState() == UserStateType.LOGGED) {
                commandFactory.getCommand(command)
                        .ifPresentOrElse(
                                cmd -> cmd.execute(update),
                                () -> sendMessage(update.getMessage().getChatId(),
                                        session.getState() == UserStateType.LOGGED
                                                ? MessagesTelegram.LOGIN_MESSAGE
                                                : MessagesTelegram.START_MESSAGE)
                        );
            } else {
                commandFactory.getCommand(userSessionService.getUserSession(chatId).getCommand())
                        .ifPresent(cmd -> cmd.execute(update));
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    public void sendMessage(Long chat_id, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chat_id)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.warn("Telegram error: {} ", e.getMessage());
        }
    }

    public Boolean checkUserLogin(Update update) {
        if (userSessionService.getUserSession(update.getMessage().getChatId()).getState() == UserStateType.LOGGED) {
            sendMessage(update.getMessage().getChatId(), MessagesTelegram.LOGIN_MESSAGE);
            return true;
        }
        return false;
    }
    public Boolean checkUserNotLogin(Update update) {
        if (userSessionService.getUserSession(update.getMessage().getChatId()).getState() != UserStateType.LOGGED) {
            sendMessage(update.getMessage().getChatId(), MessagesTelegram.NO_LOGIN_MESSAGE);
            return true;
        }
        return false;
    }
}
