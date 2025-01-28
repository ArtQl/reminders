package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.ListRemindersDto;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.exception.NotFoundException;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.ListRemindersEntity;
import ru.artq.reminders.store.repository.ListRemindersRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ListRemindersService {
    private final ListRemindersRepository listRepository;

    @Transactional(readOnly = true)
    public ListRemindersDto findList(Long listId) {
        ListRemindersEntity list = findById(listId);
        return ConverterDto.listRemindersEntityToDto(list);
    }

    @Transactional
    public ListRemindersDto createList(String title) {
        checkListExists(title);
        ListRemindersEntity list = listRepository
                .saveAndFlush(ListRemindersEntity.builder().name(title).build());
        return ConverterDto.listRemindersEntityToDto(list);
    }

    @Transactional
    public ListRemindersDto updateList(Long listId, String title) {
        ListRemindersEntity list = findById(listId);
        checkListExists(title, listId);
        list.setName(title);
        list = listRepository.saveAndFlush(list);
        return ConverterDto.listRemindersEntityToDto(list);
    }

    @Transactional
    public Boolean deleteList(Long listId) {
        findById(listId);
        listRepository.deleteById(listId);
        return true;
    }

    private ListRemindersEntity findById(Long listId) {
        return listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException
                        ("List Reminders with ID '%d' not exist.".formatted(listId)));
    }

    private void checkListExists(String title) {
        listRepository.findByTitle(title).ifPresent((list) -> {
            throw new AlreadyExistsException("The list '%s' already exists.".formatted(title));
        });
    }

    private void checkListExists(String title, Long listId) {
        listRepository.findByTitle(title)
                .filter(list -> !Objects.equals(list.getId(), listId))
                .ifPresent(pr -> {
                    throw new AlreadyExistsException("Project '%s' already exists.".formatted(title));
                });
    }
}
