package ru.artq.reminders.api.telegram;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSession {
    private UserStateType state = UserStateType.START;
    private String command = "/new";
    private Long userId;
    private String email;
    private String password;
    private String title;
    private String description;
    private String priority;
    private LocalDateTime dateTime;
}
