package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.repository.ReminderRepository;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderEntity getReminders() {
        return null;
    }


}
