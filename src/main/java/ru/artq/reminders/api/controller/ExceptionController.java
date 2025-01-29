package ru.artq.reminders.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.artq.reminders.api.dto.ErrorDto;
import ru.artq.reminders.api.exception.AlreadyExistsException;
import ru.artq.reminders.api.exception.BadRequestException;
import ru.artq.reminders.api.exception.NotFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({AlreadyExistsException.class, BadRequestException.class})
    public ResponseEntity<ErrorDto> badRequestException(RuntimeException e) {
        log.warn("Bad Request: {}", e.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> notFoundException(RuntimeException e) {
        log.warn("Not Found Exception: {}", e.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> exception(Exception e) {
        log.warn("Exception: {}", e.getMessage());
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ErrorDto> buildResponseEntity(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(ErrorDto.builder()
                .httpStatus(httpStatus.getReasonPhrase())
                .description(message).build());
    }
}
