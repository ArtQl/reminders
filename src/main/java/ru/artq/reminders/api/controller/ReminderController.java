package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;
import ru.artq.reminders.api.controller.helper.ValidateController;

@RestController
@RequestMapping("api/reminder-list/{list-id}/reminder")
@RequiredArgsConstructor
public class ReminderController {
    private final static String FIND_REMINDER = "{reminder-id}";
    private final static String CREATE_REMINDER = "";
    private final static String UPDATE_REMINDER = "{reminder-id}";
    private final static String DELETE_REMINDER = "{reminder-id}";

    private final ReminderService remindersService;

    @GetMapping(FIND_REMINDER)
    public ReminderDto findReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId) {
        ValidateController.checkListId(listId);
        ValidateController.checkReminderId(reminderId);
        return remindersService.findReminder(listId, reminderId);
    }

    @PostMapping(CREATE_REMINDER)
    public ReminderDto createReminder(
            @PathVariable("list-id") Long listId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority) {
        ValidateController.checkListId(listId);
        ValidateController.checkReminderName(name);
        ValidateController.checkPriority(priority);
        return remindersService.createReminder
                (listId, name, description, priority);
    }

    @PutMapping(UPDATE_REMINDER)
    public ReminderDto updateReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority) {
        ValidateController.checkListId(listId);
        ValidateController.checkReminderId(reminderId);
        ValidateController.checkReminderName(name);
        ValidateController.checkPriority(priority);
        return remindersService.updateReminder
                (listId, reminderId, name, description, priority);
    }

    @DeleteMapping(DELETE_REMINDER)
    public ResponseEntity<Boolean> deleteReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId) {
        ValidateController.checkListId(listId);
        ValidateController.checkReminderId(reminderId);
        remindersService.deleteReminder(listId, reminderId);
        return ResponseEntity.ok(true);
    }
}