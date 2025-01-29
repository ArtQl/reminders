package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.controller.helper.ValidateController;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.service.ReminderService;

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
        ValidateController.checkId(listId);
        ValidateController.checkId(reminderId);
        return remindersService.findReminder(listId, reminderId);
    }

    @PostMapping(CREATE_REMINDER)
    public ReminderDto createReminder(
            @PathVariable("list-id") Long listId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority) {
        ValidateController.checkId(listId);
        ValidateController.checkTitle(title);
        return remindersService.createReminder(listId, title, description, priority);
    }

    @PutMapping(UPDATE_REMINDER)
    public ReminderDto updateReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority) {
        ValidateController.checkId(listId);
        ValidateController.checkId(reminderId);
        ValidateController.checkTitle(title);
        return remindersService.updateReminder(
                listId, reminderId, title, description, priority);
    }

    @DeleteMapping(DELETE_REMINDER)
    public ResponseEntity<Boolean> deleteReminder(
            @PathVariable("list-id") Long listId,
            @PathVariable("reminder-id") Long reminderId) {
        ValidateController.checkId(listId);
        ValidateController.checkId(reminderId);
        remindersService.deleteReminder(listId, reminderId);
        return ResponseEntity.ok(true);
    }
}