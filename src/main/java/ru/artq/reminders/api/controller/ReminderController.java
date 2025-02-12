package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.controller.helper.ValidateController;
import ru.artq.reminders.api.dto.ReminderDto;
import ru.artq.reminders.api.exception.BadRequestException;
import ru.artq.reminders.api.service.ReminderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/users/{user-id}/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final static String FIND_REMINDER = "{reminder-id}";
    private final static String FIND_REMINDERS_BY_FILTERS = "";
    private final static String CREATE_REMINDER = "";
    private final static String UPDATE_REMINDER = "{reminder-id}";
    private final static String DELETE_REMINDER = "{reminder-id}";

    private final ReminderService remindersService;

    @GetMapping(FIND_REMINDER)
    public ReminderDto findReminder(
            @PathVariable("user-id") Long userId,
            @PathVariable(value = "reminder-id") Long reminderId) {
        ValidateController.checkId(reminderId);
        return remindersService.findReminder(userId, reminderId);
    }

    @GetMapping(FIND_REMINDERS_BY_FILTERS)
    public List<ReminderDto> findReminderByFilters(
            @PathVariable("user-id") Long userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime time,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {
        ValidateController.checkId(userId);
        ValidateController.checkTitle(title);
        if (limit <= 0) {
            throw new BadRequestException("Limit value: '%s' is not correct.".formatted(limit));
        }
        if (offset < 0) {
            throw new BadRequestException("Offset value: '%s' is not correct.".formatted(offset));
        }
        return remindersService.findReminder(userId, title, date, time, order, limit, offset);
    }

    @PostMapping(CREATE_REMINDER)
    public ReminderDto createReminder(
            @PathVariable("user-id") Long userId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime remind) {
        ValidateController.checkId(userId);
        ValidateController.checkTitle(title);
        return remindersService.createReminder(userId, title, description, priority, remind);
    }

    @PutMapping(UPDATE_REMINDER)
    public ReminderDto updateReminder(
            @PathVariable("user-id") Long userId,
            @PathVariable("reminder-id") Long reminderId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime remind) {
        ValidateController.checkId(userId);
        ValidateController.checkId(reminderId);
        return remindersService.updateReminder(userId, reminderId, title, description, priority, remind);
    }

    @DeleteMapping(DELETE_REMINDER)
    public ResponseEntity<Boolean> deleteReminder(
            @PathVariable("user-id") Long userId,
            @PathVariable("reminder-id") Long reminderId) {
        ValidateController.checkId(userId);
        ValidateController.checkId(reminderId);
        remindersService.deleteReminder(userId, reminderId);
        return ResponseEntity.ok(true);
    }
}