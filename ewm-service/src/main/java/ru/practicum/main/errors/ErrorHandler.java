package ru.practicum.main.errors;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.dto.ApiError;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.ForbiddenException;
import ru.practicum.main.exception.NotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage(),
                errors,
                "validation error",
                LocalDateTime.now()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {

        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName());
        }
        ApiError error = new ApiError(
                HttpStatus.CONFLICT,
                e.getLocalizedMessage(),
                errors,
                "Integrity constraint has been violated.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {

        List<String> errors = new ArrayList<>();
        errors.add(e.getStackTrace().toString());
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND,
                e.getLocalizedMessage(),
                errors,
                "The required object was not found.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException e) {

        List<String> errors = new ArrayList<>();
        errors.add(e.getStackTrace().toString());
        ApiError error = new ApiError(
                HttpStatus.FORBIDDEN,
                e.getLocalizedMessage(),
                errors,
                "For the requested operation the conditions are not met.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleForbiddenException(BadRequestException e) {

        List<String> errors = new ArrayList<>();
        errors.add(e.getStackTrace().toString());
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage(),
                errors,
                "Bad request.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiError> handleConflictException(ConflictException e) {

        List<String> errors = new ArrayList<>();
        errors.add(e.getStackTrace().toString());
        ApiError error = new ApiError(
                HttpStatus.CONFLICT,
                e.getLocalizedMessage(),
                errors,
                "Bad request.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
