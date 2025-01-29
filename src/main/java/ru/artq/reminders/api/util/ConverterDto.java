package ru.artq.reminders.api.util;

import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.dto.ReminderListDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.ReminderListEntity;

public class ConverterDto {
    public static ReminderDto reminderEntityToDto(ReminderEntity entity) {
        return ReminderDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .priority(entity.getPriority().toString())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ReminderListDto reminderListEntityToDto(ReminderListEntity entity) {
        return ReminderListDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .reminders(entity.getReminders()
                        .stream().map(ConverterDto::reminderEntityToDto).toList())
                .build();
    }
}
