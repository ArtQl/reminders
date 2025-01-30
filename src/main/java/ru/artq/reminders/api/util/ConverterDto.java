package ru.artq.reminders.api.util;

import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.dto.ReminderListDto;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.entity.UserEntity;

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

    public static UserDto userEntityToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .reminderLists(entity.getReminderLists()
                        .stream().map(ConverterDto::reminderListEntityToDto).toList())
                .build();
    }
}
