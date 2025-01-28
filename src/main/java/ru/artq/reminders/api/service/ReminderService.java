package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.repository.ReminderRepository;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderEntity getReminders() {
        return null;
    }


    public ReminderDto findReminder(Long listId, Long reminderId) {
        return null;
    }

    public ReminderDto createReminder(Long listId, String name) {
        return null;
    }

    public ReminderDto createReminder(Long listId, String name, String description, String priority) {
        return null;
    }

    public ReminderDto updateReminder(Long listId, Long reminderId, String name) {
        return null;
    }

    public ReminderDto updateReminder(Long listId, Long reminderId, String name, String description, String priority) {
        return null;
    }

    public ResponseEntity<Boolean> deleteReminder(Long listId, Long reminderId) {
        return null;
    }
}
