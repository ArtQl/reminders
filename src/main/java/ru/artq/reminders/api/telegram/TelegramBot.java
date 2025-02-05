package ru.artq.reminders.api.telegram;

import jakarta.annotation.PostConstruct;
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

    private TelegramClient telegramClient;

    private final CommandFactory commandFactory;
    private final UserSessionService userSessionService;

    @PostConstruct
    public void init() {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

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
                                () -> sendMessage(chatId,
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

    public Boolean checkUserLogin(long chatId) {
        if (userSessionService.getUserSession(chatId).getState() == UserStateType.LOGGED) {
            sendMessage(chatId, MessagesTelegram.LOGIN_MESSAGE);
            return true;
        }
        return false;
    }
    public Boolean checkUserNotLogin(long chatId) {
        if (userSessionService.getUserSession(chatId).getState() != UserStateType.LOGGED) {
            sendMessage(chatId, MessagesTelegram.NO_LOGIN_MESSAGE);
            return true;
        }
        return false;
    }
}
