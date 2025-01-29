package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.service.helper.ServiceHelper;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.repository.ReminderListRepository;
import ru.artq.reminders.store.repository.ReminderRepository;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderListRepository reminderListRepository;
    private final ServiceHelper serviceHelper;

    @Transactional(readOnly = true)
    public ReminderDto findReminder(Long listId, Long reminderId) {
        ReminderListEntity list = serviceHelper.findListById(listId);
        ReminderEntity entity = serviceHelper.findReminderById(list, reminderId);
        return ConverterDto.reminderEntityToDto(entity);
    }

    @Transactional
    public ReminderDto createReminder(
            Long listId, String title,
            String description, String priority) {
        if (reminderRepository.existsByTitle(title)) {
            throw new AlreadyExistsException("Reminder with title '%s' already exists.".formatted(title));
        }
        ReminderListEntity list = serviceHelper.findListById(listId);
        ReminderEntity entity = ReminderEntity.builder()
                .title(title).reminderList(list)
                .build();
        entity.updatePriority(priority);
        entity.updateDescription(description != null ? description : "");
        entity = reminderRepository.saveAndFlush(entity);
        list.getReminders().add(entity);
        reminderListRepository.save(list);
        return ConverterDto.reminderEntityToDto(entity);
    }

    @Transactional
    public ReminderDto updateReminder(
            Long listId, Long reminderId, String title,
            String description, String priority) {
        serviceHelper.checkReminderTitleExists(title, reminderId);
        ReminderListEntity list = serviceHelper.findListById(listId);
        ReminderEntity entity = serviceHelper.findReminderById(list, reminderId);
        entity.setTitle(title);
        entity.updatePriority(priority);
        entity.updateDescription(description);
        return ConverterDto.reminderEntityToDto(
                reminderRepository.save(entity));
    }

    @Transactional
    public void deleteReminder(Long listId, Long reminderId) {
        ReminderListEntity list = serviceHelper.findListById(listId);
        ReminderEntity reminder = serviceHelper.findReminderById(list, reminderId);
        list.getReminders().remove(reminder);
        reminderListRepository.save(list);
    }
}
