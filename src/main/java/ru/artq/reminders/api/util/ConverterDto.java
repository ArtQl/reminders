package ru.artq.reminders.api.util;

import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.UserEntity;

public class ConverterDto {
    public static ReminderDto reminderEntityToDto(ReminderEntity entity) {
        return ReminderDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .priority(entity.getPriority().toString())
                .createdAt(entity.getCreatedAt())
                .remind(entity.getRemind())
                .userId(entity.getUser().getId())
                .build();
    }

    public static UserDto userEntityToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .telegramChatId(entity.getTelegramChatId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .reminders(entity.getReminders()
                        .stream().map(ConverterDto::reminderEntityToDto).toList())
                .build();
    }
}