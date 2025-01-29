package ru.artq.reminders.api.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;
import ru.artq.reminders.store.entity.ReminderListEntity;

import java.time.Instant;
import java.util.ArrayList;
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
    @NonNull
    private String password;

    private Instant createdAt;

    private List<ReminderListEntity> reminderLists;
}
