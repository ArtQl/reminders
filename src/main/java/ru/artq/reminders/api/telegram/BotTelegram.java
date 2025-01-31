package ru.artq.reminders.api.telegram;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (!text.startsWith("/login") && user == null) {
                sendMessage(chatId, MessagesTelegram.LOGIN_MESSAGE);
            } else if(text.startsWith("/login")) {
                handleLogin(chatId, text, update.getMessage().getFrom().getUserName());
            }

            if (text.startsWith("/new")) {
                handleNew(chatId, text);
            } else if (text.startsWith("/find")) {
                handleFind(chatId);
            } else {
                sendMessage(chatId, MessagesTelegram.START_MESSAGE);
            }
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
            sendMessage(chatId, "Напоминание добавлено.");
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private void handleFind(long chatId) {
        StringBuilder sb = new StringBuilder("Список ваших напоминаний:\n");
        try {
            List<ReminderDto> list= reminderService.findReminder(user.getId());
            list.forEach(rem -> {
                sb.append(rem);
                sb.append("\n");
            });

            sendMessage(chatId, sb.toString());
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private void handleLogin(long chatId, String text, String username) {
        if (user != null) {
            sendMessage(chatId, "Вы уже успешно вошли в системе!");
        }
        String[] parts = text.split(" ");
        if (parts.length != 3) {
            sendMessage(chatId, "Неверный формат. Используйте: /login [email] [password]");
            return;
        }
        try {
            user = userService.createUser(chatId, username, parts[1], parts[2]);
            sendMessage(chatId, "Вы успешно вошли в систему!");
        } catch (RuntimeException e) {
            sendMessage(chatId, e.getMessage());
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
