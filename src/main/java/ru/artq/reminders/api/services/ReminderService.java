package ru.artq.reminders.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artq.reminders.store.entities.ReminderEntity;
import ru.artq.reminders.store.repositories.ReminderRepository;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderEntity getReminders() {
        return null;
    }


}
