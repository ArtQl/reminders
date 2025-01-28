package ru.artq.reminders.api.util;

import ru.artq.reminders.api.dto.ReminderListDto;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.entity.ReminderEntity;

public class ConverterDto {

    public static ReminderDto reminderEntityToDto(ReminderEntity entity) {
        return ReminderDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .priority(entity.getPriority().toString())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ReminderListDto reminderListEntityToDto(ReminderListEntity entity) {
        return ReminderListDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .reminders(entity.getReminders()
                        .stream().map(ConverterDto::reminderEntityToDto).toList())
                .build();
    }
}
