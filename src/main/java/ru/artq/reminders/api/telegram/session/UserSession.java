package ru.artq.reminders.api.telegram.session;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSession {
    private UserStateType state = UserStateType.START;
    private String command = "/start";
    private Message message;
    private Long userId;
    private String email;
    private String password;
    private String title;
    private String description;
    private String priority;
    private LocalDateTime dateTime;

    public void clear() {
        email = null;
        password = null;
        title = null;
        description = null;
        priority = null;
        dateTime = null;
    }
}
