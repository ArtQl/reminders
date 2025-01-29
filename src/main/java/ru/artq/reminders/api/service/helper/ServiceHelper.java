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

    public void checkListTitleExists(String title, Long listId) {
        reminderListRepository.findByTitle(title)
                .filter(list -> !Objects.equals(list.getId(), listId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("List Reminders with title: '%s' already exists.".formatted(title));
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

    public void checkReminderTitleExists(String title, Long reminderId) {
        reminderRepository.findByTitle(title)
                .filter(reminder -> !Objects.equals(reminder.getId(), reminderId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("Reminder with title: '%s' already exists.".formatted(title));
                });
    }
}
