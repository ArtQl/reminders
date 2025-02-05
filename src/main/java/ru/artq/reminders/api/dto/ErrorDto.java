package ru.artq.reminders.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    private final String httpStatus;

    private final String description;
}
