package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.util.ControllerValidate;

@RestController
@RequestMapping("api/reminder-list")
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
        ControllerValidate.checkListId(listId);
        ControllerValidate.checkReminderId(reminderId);
        return remindersService.findReminder(listId, reminderId);
    }

    @PostMapping(CREATE_REMINDER)
    public ReminderDto createReminder(
            @PathVariable("list-id") Long listId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority
    ) {
        ControllerValidate.checkListId(listId);
        ControllerValidate.checkReminderName(name);
        ReminderDto reminderDto = remindersService.createReminder(listId, name);
        reminderDto = remindersService.createReminder(listId, name, description, priority);
        return null;
    }

    @PutMapping(UPDATE_REMINDER)
    public ReminderDto updateReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority) {
        ControllerValidate.checkListId(listId);
        ControllerValidate.checkReminderId(reminderId);
        ControllerValidate.checkReminderName(name);
        ReminderDto reminderDto = remindersService.updateReminder(listId, reminderId, name);
        reminderDto = remindersService.updateReminder(listId, reminderId, name, description, priority);
        return null;
    }

    @DeleteMapping(DELETE_REMINDER)
    public ResponseEntity<Boolean> deleteReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId) {
        ControllerValidate.checkListId(listId);
        ControllerValidate.checkReminderId(reminderId);
        return remindersService.deleteReminder(listId, reminderId);
    }
}