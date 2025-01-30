package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.exception.NotFoundException;
import ru.artq.reminders.api.service.helper.ServiceHelper;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.UserEntity;
import ru.artq.reminders.store.repository.ReminderRepository;
import ru.artq.reminders.store.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final UserRepository userRepository;
    private final ReminderRepository reminderRepository;
    private final ServiceHelper serviceHelper;

    @Transactional(readOnly = true)
    public ReminderDto findReminder(Long userId, Long reminderId) {
        UserEntity user = serviceHelper.findUserById(userId);
        ReminderEntity entity = serviceHelper.findReminderById(user, reminderId);
        return ConverterDto.reminderEntityToDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ReminderDto> findReminder(Long userId, String title,
                                    LocalDate date, LocalTime time,
                                    String order, Integer limit, Integer offset) {
        List<ReminderEntity> reminders = reminderRepository.findByUserId(userId);
        if (reminders.isEmpty()) {
            throw new NotFoundException("User reminder %d not found".formatted(userId));
        }
        return reminders.stream()
                .filter(r -> title == null || title.isBlank() ||
                        r.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(r -> date == null || r.getRemind().toLocalDate().isEqual(date))
                .filter(r -> time == null ||  r.getRemind().toLocalTime().equals(time))
                .sorted((r1, r2) -> {
                    if (order.equals("desc") || order.isBlank()) {
                        return r2.getId().compareTo(r1.getId());
                    } else {
                        return r1.getId().compareTo(r2.getId());
                    }
                })
                .skip(offset)
                .limit(limit)
                .map(ConverterDto::reminderEntityToDto)
                .toList();
    }

    @Transactional
    public ReminderDto createReminder(Long userId, String title,
                                      String description, String priority) {
        if (reminderRepository.existsByTitle(title)) {
            throw new AlreadyExistsException("Reminder with title '%s' already exists.".formatted(title));
        }
        UserEntity user = serviceHelper.findUserById(userId);
        ReminderEntity entity = ReminderEntity.builder()
                .title(title).user(user).build();
        entity.updatePriority(priority);
        entity.updateDescription(description != null ? description : "");
        entity = reminderRepository.saveAndFlush(entity);
        user.getReminders().add(entity);
        userRepository.save(user);
        return ConverterDto.reminderEntityToDto(entity);
    }

    @Transactional
    public ReminderDto updateReminder(Long userId, Long reminderId, String title,
                                      String description, String priority) {
        serviceHelper.checkReminderTitleExists(title, reminderId);
        UserEntity user = serviceHelper.findUserById(userId);
        ReminderEntity entity = serviceHelper.findReminderById(user, reminderId);
        entity.setTitle(title);
        entity.updatePriority(priority);
        entity.updateDescription(description);
        return ConverterDto.reminderEntityToDto(reminderRepository.save(entity));
    }

    @Transactional
    public void deleteReminder(Long userId, Long reminderId) {
        UserEntity user = serviceHelper.findUserById(userId);
        ReminderEntity reminder = serviceHelper.findReminderById(user, reminderId);
        user.getReminders().remove(reminder);
        userRepository.save(user);
    }
}
