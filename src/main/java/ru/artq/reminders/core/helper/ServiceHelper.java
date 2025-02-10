package ru.artq.reminders.core.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artq.reminders.core.exception.AlreadyExistsException;
import ru.artq.reminders.core.exception.NotFoundException;
import ru.artq.reminders.core.entity.ReminderEntity;
import ru.artq.reminders.core.entity.UserEntity;
import ru.artq.reminders.core.repository.ReminderRepository;
import ru.artq.reminders.core.repository.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ServiceHelper {
    private final UserRepository userRepository;
    private final ReminderRepository reminderRepository;

    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("User with ID '%d' not found.".formatted(userId)));
    }

    public ReminderEntity findReminderById(Long userId, Long reminderId) {
        return reminderRepository.findById(reminderId)
                .filter(rem -> rem.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException
                        ("Reminder with ID '%d' not found.".formatted(reminderId)));
    }

    public void checkReminderTitleExists(String title, Long reminderId) {
        reminderRepository.findByTitle(title)
                .filter(rem -> !Objects.equals(rem.getId(), reminderId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("Reminder with title: '%s' already exists."
                            .formatted(title));
                });
    }

    public void checkUsernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("Username '%s' already exists.".formatted(username));
        }
    }

    public void checkEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email '%s' already exists.".formatted(email));
        }
    }
}