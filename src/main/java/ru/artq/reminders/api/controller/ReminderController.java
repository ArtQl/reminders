package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;

@RestController
@RequestMapping("api/list-reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final static String FIND_REMINDER = "{list-id}/{reminder-id}";
    private final static String CREATE_REMINDER = "{list-id}";
    private final static String UPDATE_REMINDER = "{list-id}/{reminder-id}";
    private final static String DELETE_REMINDER = "{list-id}/{reminder-id}";

    private final ReminderService remindersService;

    @GetMapping(FIND_REMINDER)
    public ReminderDto findReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId) {
        return null;
    }

    @PostMapping(CREATE_REMINDER)
    public ReminderDto createReminder(
            @PathVariable("list-id") Long listId,
            @RequestParam String title,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String priority
    ) {
        return null;
    }

    @PutMapping(UPDATE_REMINDER)
    public ReminderDto updateReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId,
            @RequestParam String title,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String priority) {
        return null;
    }

    @DeleteMapping(DELETE_REMINDER)
    public Boolean deleteReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId) {
        return null;
    }
}