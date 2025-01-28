package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.ReminderListDto;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.exception.NotFoundException;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.ReminderListEntity;
import ru.artq.reminders.store.repository.ReminderListRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReminderListService {
    private final ReminderListRepository reminderListRepository;

    @Transactional(readOnly = true)
    public ReminderListDto findList(Long listId) {
        ReminderListEntity list = findById(listId);
        return ConverterDto.reminderListEntityToDto(list);
    }

    @Transactional
    public ReminderListDto createList(String name) {
        checkListExists(name);
        ReminderListEntity list = reminderListRepository
                .saveAndFlush(ReminderListEntity.builder().name(name).build());
        return ConverterDto.reminderListEntityToDto(list);
    }

    @Transactional
    public ReminderListDto updateList(Long listId, String name) {
        ReminderListEntity list = findById(listId);
        checkListExists(name, listId);
        list.setName(name);
        list = reminderListRepository.saveAndFlush(list);
        return ConverterDto.reminderListEntityToDto(list);
    }

    @Transactional
    public Boolean deleteList(Long listId) {
        findById(listId);
        reminderListRepository.deleteById(listId);
        return true;
    }

    private ReminderListEntity findById(Long listId) {
        return reminderListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException
                        ("List Reminders with ID '%d' not exist.".formatted(listId)));
    }

    private void checkListExists(String name) {
        reminderListRepository.findByName(name).ifPresent((list) -> {
            throw new AlreadyExistsException("The list '%s' already exists.".formatted(name));
        });
    }

    private void checkListExists(String name, Long listId) {
        reminderListRepository.findByName(name)
                .filter(list -> !Objects.equals(list.getId(), listId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("Project '%s' already exists.".formatted(name));
                });
    }
}
