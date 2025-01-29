package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.helper.ServiceHelper;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.ReminderEntity;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.entity.ReminderPriority;
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
            Long listId, String name,
            String description, String priority) {
        serviceHelper.checkReminderNameExists(name);

        ReminderListEntity list = serviceHelper.findListById(listId);

        ReminderEntity entity = ReminderEntity.builder()
                .name(name).reminderList(list).build();
        setReminderPriorityAndDescription(entity, description, priority);

        entity = reminderRepository.saveAndFlush(entity);

        list.getReminders().add(entity);
        reminderListRepository.save(list);

        return ConverterDto.reminderEntityToDto(entity);
    }

    @Transactional
    public ReminderDto updateReminder(
            Long listId, Long reminderId, String name,
            String description, String priority) {
        serviceHelper.checkReminderNameExists(name);

        ReminderListEntity list = serviceHelper.findListById(listId);

        ReminderEntity entity = serviceHelper.findReminderById(list, reminderId);

        setReminderPriorityAndDescription(entity, description, priority);

        entity = reminderRepository.saveAndFlush(entity);

        return ConverterDto.reminderEntityToDto(entity);
    }

    @Transactional
    public void deleteReminder(Long listId, Long reminderId) {
        ReminderListEntity list = serviceHelper.findListById(listId);
        ReminderEntity reminder = serviceHelper.findReminderById(list, reminderId);
        list.getReminders().remove(reminder);
        reminderListRepository.save(list);
    }

    private void setReminderPriorityAndDescription(
            ReminderEntity entity, String description, String priority) {
        if (description != null) {
            entity.setDescription(description);
        }
        if (priority != null) {
            entity.setPriority(ReminderPriority.valueOf(priority));
        }
    }
}
