package ru.artq.reminders.api.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderDto {
    private Long id;

    @NonNull
    private String title;

    private String description;

    private String priority;

    private Instant createdAt;
}
