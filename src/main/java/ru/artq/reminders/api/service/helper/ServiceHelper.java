package ru.artq.reminders.api.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.exception.NotFoundException;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.repository.ReminderListRepository;
import ru.artq.reminders.store.repository.ReminderRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ServiceHelper {
    private final ReminderListRepository reminderListRepository;
    private final ReminderRepository reminderRepository;

    public ReminderListEntity findListById(Long listId) {
        return reminderListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException
                        ("List Reminders with ID '%d' not found.".formatted(listId)));
    }

    public void checkListNameExists(String name, Long listId) {
        reminderListRepository.findByName(name)
                .filter(list -> !Objects.equals(list.getId(), listId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("List Reminders with name: '%s' already exists.".formatted(name));
                });
    }

    public ReminderEntity findReminderById(Long listId) {
        return reminderRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException
                        ("Reminder with ID '%d' not found.".formatted(listId)));
    }

    public ReminderEntity findReminderById(ReminderListEntity list, Long reminderId) {
        return list.getReminders().stream()
                .filter(reminder -> reminder.getId().equals(reminderId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException
                        ("Reminder with ID '%d' not found.".formatted(reminderId)));
    }

    public void checkReminderNameExists(String name, Long reminderId) {
        reminderRepository.findByName(name)
                .filter(reminder -> !Objects.equals(reminder.getId(), reminderId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("Reminder with name: '%s' already exists.".formatted(name));
                });
    }
}
