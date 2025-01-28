package ru.artq.reminders.api.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListRemindersDto {
    private Long id;

    @NonNull
    private String name;

    private List<ReminderDto> reminders;
}
