package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.ReminderListDto;
import ru.artq.reminders.api.service.helper.ServiceHelper;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.repository.ReminderListRepository;

@Service
@RequiredArgsConstructor
public class ReminderListService {
    private final ReminderListRepository reminderListRepository;
    private final ServiceHelper serviceHelper;

    @Transactional(readOnly = true)
    public ReminderListDto findList(Long listId) {
        ReminderListEntity list = serviceHelper.findListById(listId);
        return ConverterDto.reminderListEntityToDto(list);
    }

    @Transactional
    public ReminderListDto createList(String name) {
        serviceHelper.checkListNameExists(name);
        ReminderListEntity list = reminderListRepository
                .saveAndFlush(ReminderListEntity.builder().name(name).build());
        return ConverterDto.reminderListEntityToDto(list);
    }

    @Transactional
    public ReminderListDto updateList(Long listId, String name) {
        ReminderListEntity list = serviceHelper.findListById(listId);
        serviceHelper.checkListNameExists(name, listId);
        list.setName(name);
        list = reminderListRepository.saveAndFlush(list);
        return ConverterDto.reminderListEntityToDto(list);
    }

    @Transactional
    public void deleteList(Long listId) {
        ReminderListEntity list = serviceHelper.findListById(listId);
        reminderListRepository.delete(list);
    }
}
