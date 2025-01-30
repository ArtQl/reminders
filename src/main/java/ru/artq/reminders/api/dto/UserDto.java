package ru.artq.reminders.api.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String email;

    private Instant createdAt;

    private List<ReminderListDto> reminderLists;
}
