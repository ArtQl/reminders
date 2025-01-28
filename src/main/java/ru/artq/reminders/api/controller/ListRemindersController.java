package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.exception.BadRequestException;
import ru.artq.reminders.api.service.ListRemindersService;

@RestController
@RequestMapping("api/list-reminders")
@RequiredArgsConstructor
@Slf4j
public class ListRemindersController {
    private final static String FIND_LIST_REMINDERS = "{list-id}";
    private final static String CREATE_LIST_REMINDERS = "";
    private final static String UPDATE_LIST_REMINDERS = "{list-id}";
    private final static String DELETE_LIST_REMINDERS = "{list-id}";

    private final ListRemindersService listService;

    @GetMapping(FIND_LIST_REMINDERS)
    public ReminderDto findListReminders(
            @PathVariable("list-id") Long listId) {
        checkListId(listId);
        return listService.findList(listId);
    }

    @PostMapping(CREATE_LIST_REMINDERS)
    public ReminderDto createListReminders(
            @RequestParam String title) {
        checkListTitle(title);
        return listService.createList(title);
    }

    @PutMapping(UPDATE_LIST_REMINDERS)
    public ReminderDto updateListReminders(
            @PathVariable("list-id") Long listId,
            @RequestParam String title) {
        checkListId(listId);
        checkListTitle(title);
        return listService.updateList(listId, title);
    }

    @DeleteMapping(DELETE_LIST_REMINDERS)
    public Boolean deleteListReminders(
            @PathVariable("list-id") Long listId) {
        checkListId(listId);
        return listService.deleteList(listId);
    }

    private void checkListId(Long id) {
        if (id == null || id <= 0) {
            log.info("List id: {} is not correct.", id);
            throw new BadRequestException("List id is not correct.");
        }
    }

    private void checkListTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            log.info("Title list: {} is not correct.", title);
            throw new BadRequestException("Title list is not correct.");
        }
    }
}