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
import ru.artq.reminders.api.telegram.command.CommandFactory;
import ru.artq.reminders.api.telegram.session.UserSession;
import ru.artq.reminders.api.telegram.session.UserSessionService;

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
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() && !update.getMessage().hasText()) return;

        long chatId = update.getMessage().getChatId();
        UserSession session = userSessionService.getUserSession(chatId);
        String commandKey = update.getMessage().getText().trim().toLowerCase().split(" ")[0];

        switch (session.getState()) {
            case START -> handleStartState(update, commandKey, chatId);
            case LOGGED -> handleLoggedState(update, commandKey, chatId);
            case REGISTRATION, LOGIN, CREATE_REMINDER ->
                    executeCommand(session.getCommand(), update, chatId, "Неизвестная команда!");
        }
    }

    private void handleStartState(Update update, String commandKey, long chatId) {
        if (commandKey.equals("/registration") || commandKey.equals("/login")) {
            executeCommand(commandKey, update, chatId, MessagesTelegram.START_MESSAGE);
        } else {
            sendMessage(chatId, MessagesTelegram.START_MESSAGE);
        }
    }

    private void handleLoggedState(Update update, String commandKey, long chatId) {
        if (!commandKey.equals("/registration") && !commandKey.equals("/login")) {
            executeCommand(commandKey, update, chatId, MessagesTelegram.LOGIN_MESSAGE);
        } else {
            sendMessage(chatId, MessagesTelegram.LOGIN_MESSAGE);
        }
    }

    private void executeCommand(String command, Update update,
                                long chatId, String message) {
        commandFactory.getCommand(command)
                .ifPresentOrElse(
                        cmd -> {
                            log.info("Executing command: {}", command);
                            cmd.execute(update);
                        },
                        () -> {
                            log.warn("Unknown command: {}", command);
                            sendMessage(chatId, message);
                        }
                );
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage());

        }
    }
}
