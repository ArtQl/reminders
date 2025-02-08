package ru.artq.reminders.api.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artq.reminders.api.telegram.command.CommandFactory;
import ru.artq.reminders.api.telegram.session.UserSession;
import ru.artq.reminders.api.telegram.session.UserSessionService;
import ru.artq.reminders.api.telegram.session.UserStateType;


@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final CommandFactory commandFactory;
    private final UserSessionService userSessionService;
    private final Environment env;
    private final TelegramClient telegramClient;
//    private final OkHttpClient okHttpClient;

    @Override
    public String getBotToken() {
        return env.getProperty("telegram.bot.token");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            UserSession session = userSessionService.getUserSession(chatId);
            String commandKey = update.getMessage().getText().trim().toLowerCase().split(" ")[0];

            switch (session.getState()) {
                case START -> handleStartState(update, commandKey, chatId);
                case LOGGED -> handleLoggedState(update, commandKey, chatId);
                case REGISTRATION, LOGIN, CREATE_REMINDER, DELETE ->
                    executeCommand(session.getCommand(), update, chatId, "Неизвестная команда!");
            }
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    deleteMessage(chatId, update.getMessage().getMessageId());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            UserSession session = userSessionService.getUserSession(chatId);
            if (session.getState().equals(UserStateType.CREATE_REMINDER)) {
                executeCommand(session.getCommand(), update, chatId, "Неизвестная команда!");
            }
        }
    }

    private void handleStartState(Update update, String commandKey, long chatId) {
        if (commandKey.equals("/registration") || commandKey.equals("/login")) {
            executeCommand(commandKey, update, chatId, env.getProperty("telegram.bot.start-message"));
        } else {
            sendMessage(chatId, env.getProperty("telegram.bot.start-message"));
        }
    }

    private void handleLoggedState(Update update, String commandKey, long chatId) {
        if (!commandKey.equals("/registration") && !commandKey.equals("/login")) {
            executeCommand(commandKey, update, chatId, env.getProperty("telegram.bot.login-message"));
        } else {
            sendMessage(chatId, env.getProperty("telegram.bot.login-message"));
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
        try {
            Message message = userSessionService.getUserSession(chatId).getMessage();
            if (message != null) {
                if (!message.getText().toLowerCase().trim().equals(text.toLowerCase().trim())) {
                    telegramClient.execute(EditMessageText.builder()
                            .chatId(chatId)
                            .messageId(message.getMessageId())
                            .text(text)
                            .build());
                }
            } else {
                Message sentMessage = telegramClient
                        .execute(SendMessage.builder()
                                .chatId(chatId)
                                .text(text)
                                .build());
                userSessionService.getUserSession(chatId).setMessage(sentMessage);
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage());
        }
    }

    public void deleteMessage(long chatId, Integer messageId) {
        try {
            telegramClient.execute(DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Failed to delete message {}: {}", messageId, e.getMessage());
        }
    }

    public void sendMessageWithKeyboard(Long chatId, String text) {
//        SendMessage message = SendMessage
//                .builder()
//                .chatId(chatId)
//                .text(text)
//                .replyMarkup(InlineKeyboardMarkup.builder()
        try {
            telegramClient.execute(EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(userSessionService.getUserSession(chatId).getMessage().getMessageId())
                    .text(text)
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(
                                            InlineKeyboardButton
                                                    .builder()
                                                    .text("Low")
                                                    .callbackData("low")
                                                    .build(),
                                            InlineKeyboardButton
                                                    .builder()
                                                    .text("Medium")
                                                    .callbackData("medium")
                                                    .build(),
                                            InlineKeyboardButton
                                                    .builder()
                                                    .text("High")
                                                    .callbackData("high")
                                                    .build()
                                    )
                            ).build())
                    .build());
        } catch (TelegramApiException e) {
            log.error("Failed to send message keyboard to chat {}: {}", chatId, e.getMessage());
        }
    }
}
