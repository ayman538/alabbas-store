package com.alabbas.store.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        response.put("timestamp", LocalDateTime.now());
        response.put("status", 400);
        response.put("message", getTranslatedMessage("validation.failed", locale));
        response.put("errors", errors);

        return response;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleResourceNotFoundException(ResourceNotFoundException ex, Locale locale) {
        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("message", getTranslatedMessage(ex.getMessage(), locale, ex.getArgs()));

        return response;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleBusinessException(BusinessException ex, Locale locale) {
        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", 409);
        response.put("message", getTranslatedMessage(ex.getMessage(), locale));

        return response;
    }

    private String getTranslatedMessage(String messageKey, Locale locale, Object... args) {
        return messageSource.getMessage(messageKey, args, messageKey, locale);
    }
}
