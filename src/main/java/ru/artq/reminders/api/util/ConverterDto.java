package ru.artq.reminders.api.util;

import ru.artq.reminders.api.dto.ListRemindersDto;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.store.entities.ListRemindersEntity;
import ru.artq.reminders.store.entities.ReminderEntity;

public class ConverterDto {

    public static ReminderDto reminderEntityToDto(ReminderEntity entity) {
        return ReminderDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .notes(entity.getNotes())
                .priority(entity.getPriority().toString())
                .time(entity.getTime())
                .build();
    }

    public static ListRemindersDto listRemindersEntityToDto(ListRemindersEntity list) {
        return ListRemindersDto.builder()
                .id(list.getId())
                .name(list.getName())
                .reminders(list.getReminders()
                        .stream().map(ConverterDto::reminderEntityToDto).toList())
                .build();
    }
}
