package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.dto.ReminderListDto;
import ru.artq.reminders.api.service.ReminderListService;
import ru.artq.reminders.api.util.ControllerValidate;

@RestController
@RequestMapping("api/reminder-list")
@RequiredArgsConstructor
public class ReminderListController {
    private final static String FIND_REMINDER_LIST = "{list-id}";
    private final static String CREATE_REMINDER_LIST = "";
    private final static String UPDATE_REMINDER_LIST = "{list-id}";
    private final static String DELETE_REMINDER_LIST = "{list-id}";

    private final ReminderListService reminderListService;

    @GetMapping(FIND_REMINDER_LIST)
    public ReminderListDto findList(@PathVariable("list-id") Long listId) {
        ControllerValidate.checkListId(listId);
        return reminderListService.findList(listId);
    }

    @PostMapping(CREATE_REMINDER_LIST)
    public ReminderListDto createList(@RequestParam String title) {
        ControllerValidate.checkListName(title);
        return reminderListService.createList(title);
    }

    @PutMapping(UPDATE_REMINDER_LIST)
    public ReminderListDto updateList(@PathVariable("list-id") Long listId,
                                      @RequestParam String title) {
        ControllerValidate.checkListId(listId);
        ControllerValidate.checkListName(title);
        return reminderListService.updateList(listId, title);
    }

    @DeleteMapping(DELETE_REMINDER_LIST)
    public ResponseEntity<Boolean> deleteList(@PathVariable("list-id") Long listId) {
        ControllerValidate.checkListId(listId);
        return ResponseEntity.ok(reminderListService.deleteList(listId));
    }
}