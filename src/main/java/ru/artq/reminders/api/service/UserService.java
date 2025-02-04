package ru.artq.reminders.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artq.reminders.api.dto.UserDto;
import ru.artq.reminders.api.exception.BadRequestException;
import ru.artq.reminders.api.exception.NotFoundException;
import ru.artq.reminders.api.service.helper.ServiceHelper;
import ru.artq.reminders.api.util.ConverterDto;
import ru.artq.reminders.store.entity.UserEntity;
import ru.artq.reminders.store.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ServiceHelper serviceHelper;

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        return ConverterDto.userEntityToDto(serviceHelper.findUserById(userId));
    }

    @Transactional(readOnly = true)
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException
                        ("User with email '%s' not found.".formatted(email)));
    }

    @Transactional(readOnly = true)
    public UserDto findUserByUsername(String username) {
        return ConverterDto.userEntityToDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username '%s' not found.".formatted(username))));
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Long findTelegramChatId(Long userId) {
        UserEntity entity =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id '%s' not found.".formatted(userId)));
        return entity.getTelegramChatId();
    }

    @Transactional(readOnly = true)
    public UserDto findUserByEmailAndPassword(String username, String password) {
        UserEntity user = userRepository.findByEmailAndPassword(username, password)
                .orElseThrow(() -> new BadRequestException("Неверный логин или пароль!"));
        return ConverterDto.userEntityToDto(user);
    }

    @Transactional
    public UserDto createUser(Long chatId, String username, String email, String password) {
        serviceHelper.checkUsernameExist(username);
        serviceHelper.checkEmailExist(email);
        UserEntity user = userRepository.saveAndFlush(
                UserEntity.builder()
                        .telegramChatId(chatId)
                        .username(username)
                        .email(email)
                        .password(password)
                        .build());
        return ConverterDto.userEntityToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long userId, String username, String email, String password) {
        UserEntity user = serviceHelper.findUserById(userId);
        if (username != null && !username.isBlank()) {
            serviceHelper.checkUsernameExist(username);
            user.setUsername(username);
        }
        if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
            serviceHelper.checkEmailExist(email);
            user.setEmail(email);
        }
        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }
        return ConverterDto.userEntityToDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with ID '%d' not found.".formatted(userId));
        }
        userRepository.deleteById(userId);
    }
}