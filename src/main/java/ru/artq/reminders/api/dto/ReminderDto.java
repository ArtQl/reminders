package ru.artq.reminders.api.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderDto {
    private Long id;

    @NonNull
    private String title;

    private LocalDateTime remind;

    private String description;

    private String priority;

    private Instant createdAt;

    private Long userId;

    private boolean completed;
}
