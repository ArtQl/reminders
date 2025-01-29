package ru.artq.reminders.api.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderListDto {
    private Long id;

    @NonNull
    private String title;

    private List<ReminderDto> reminders;
}
