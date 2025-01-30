package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.exception.NotFoundException;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.UserEntity;
import ru.artq.reminders.store.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        return ConverterDto.userEntityToDto(getUserById(userId));
    }

    @Transactional
    public UserDto createUser(String username, String email, String password) {
        checkUsernameExist(username);
        checkEmailExist(email);
        UserEntity user = userRepository.saveAndFlush(
                UserEntity.builder()
                        .username(username)
                        .email(email)
                        .password(password)
                        .build());
        return ConverterDto.userEntityToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long userId, String username, String email, String password) {
        UserEntity user = getUserById(userId);
        checkUsernameExist(username);
        checkEmailExist(email);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return ConverterDto.userEntityToDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("User with ID '%d' not found.".formatted(userId)));
    }

    private void checkUsernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("Username '%s' already exists.".formatted(username));
        }
    }
    private void checkEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email '%s' already exists.".formatted(email));
        }
    }
}
