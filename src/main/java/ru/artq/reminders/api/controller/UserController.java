package ru.artq.reminders.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artq.reminders.api.controller.helper.ValidateController;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final static String FIND_USER = "{user-id}";
    private final static String CREATE_USER = "";
    private final static String UPDATE_USER = "{user-id}";
    private final static String DELETE_USER = "{user-id}";

    private final UserService userService;

    @GetMapping(FIND_USER)
    public UserDto findUser(@PathVariable("user-id") Long userId) {
        ValidateController.checkId(userId);
        return userService.findUser(userId);
    }

    @PostMapping(CREATE_USER)
    public UserDto createUser(@RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String password) {
        ValidateController.checkUsername(username);
        ValidateController.checkEmail(email);
        ValidateController.checkPassword(password);
        return userService.createUser(username, email, password);
    }

    @PutMapping(UPDATE_USER)
    public UserDto updateUser(@PathVariable("user-id") Long userId,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String password) {
        ValidateController.checkId(userId);
        return userService.updateUser(userId, username, email, password);
    }

    @DeleteMapping(DELETE_USER)
    public ResponseEntity<Boolean> deleteUser(@PathVariable("user-id") Long userId) {
        ValidateController.checkId(userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok(true);
    }
}
