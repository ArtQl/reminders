package ru.artq.reminders.telegram;

import lombok.RequiredArgsConstructor;
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
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotTelegram implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient = new OkHttpTelegramClient(getBotToken());
    private final UserService userService;
    private final ReminderService reminderService;
    private UserDto user;

    @Override
    public void consume(Update update) {
        try {
            handleDirections(update);
        } catch (Exception e) {
            log.error("Error processing update in directions bot", e);
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

    private void handleDirections(Update update) {
        if (!update.hasMessage() && !update.getMessage().hasText()) return;

        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (!text.startsWith("/login") && user == null) {
            sendMessage(update.getMessage().getChatId(), MessagesTelegram.LOGIN_MESSAGE);
        } else if (text.startsWith("/login") && user == null) {
            handleLogin(update);
        }

        if (text.startsWith("/new")) {
            handleNew(chatId, text);
        } else if (text.startsWith("/find")) {
            handleFind(chatId, text);
        } else {
            sendMessage(chatId, MessagesTelegram.START_MESSAGE);
        }
    }

    private void handleNew(long chatId, String text) {
        String[] parts = text.split(" ");
        if (parts.length <= 1 || parts.length > 5) {
            sendMessage(chatId, "Неверный формат. Используйте: /register [title] [description] [priority] [dateTime]");
            return;
        }
        try {
            String title = parts[1];
            String description = parts[2];
            String priority = parts[3];
            LocalDateTime dateTime = LocalDateTime.parse(parts[4]);
            reminderService.createReminder(user.getId(), title, description, priority, dateTime);
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private void handleFind(long chatId, String text) {
        try {
            List<ReminderDto> list= reminderService.findReminder(user.getId());
            sendMessage(chatId, list.toString());
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private void handleLogin(Update update) {
        String[] parts = update.getMessage().getText().split(" ");
        if (parts.length != 3) {
            sendMessage(update.getMessage().getChatId(),
                    "Неверный формат. Используйте: /login [email] [password]");
            return;
        }

        try {
            user = userService.createUser(update.getMessage().getFrom().getUserName(), parts[1], parts[2]);
            sendMessage(update.getMessage().getChatId(), "Вы успешно вошли в систему!");
        } catch (RuntimeException e) {
            sendMessage(update.getMessage().getChatId(), e.getMessage());
        }
    }

    private void sendMessage(Long chat_id, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chat_id).text(text).build());
        } catch (TelegramApiException e) {
            log.warn("Telegram error: {} ", e.getMessage());
        }
    }
}
