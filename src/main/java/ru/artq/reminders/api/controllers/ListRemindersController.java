package ru.artq.reminders.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.services.ListRemindersService;

@RestController
@RequestMapping("api/list-reminders")
@RequiredArgsConstructor
public class ListRemindersController {
    private final static String FIND_LIST_REMINDERS = "{list-id}";
    private final static String CREATE_LIST_REMINDERS = "{list-id}";
    private final static String UPDATE_LIST_REMINDERS = "{list-id}";
    private final static String DELETE_LIST_REMINDERS = "{list-id}";

    private final ListRemindersService listRemindersService;

    @GetMapping(FIND_LIST_REMINDERS)
    public ReminderDto findListReminders(
            @PathVariable("list-id") Long listId) {
        return null;
    }

    @PostMapping(CREATE_LIST_REMINDERS)
    public ReminderDto createListReminders(
            @PathVariable("list-id") Long listId,
            @RequestParam String title) {
        return null;
    }

    @PutMapping(UPDATE_LIST_REMINDERS)
    public ReminderDto updateListReminders(
            @PathVariable("list-id") Long listId,
            @RequestParam String title) {
        return null;
    }

    @DeleteMapping(DELETE_LIST_REMINDERS)
    public Boolean deleteListReminders(
            @PathVariable("list-id") Long listId) {
        return null;
    }
}