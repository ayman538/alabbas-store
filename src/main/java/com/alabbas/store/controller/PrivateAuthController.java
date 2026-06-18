package com.alabbas.store.controller;

import com.alabbas.store.dto.ApiResponse;
import com.alabbas.store.service.AuthService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/private/auth")
public class PrivateAuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    public PrivateAuthController(AuthService authService, MessageSource messageSource) {
        this.authService = authService;
        this.messageSource = messageSource;
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader("Authorization") String authorizationHeader) {
        authService.logout(authorizationHeader);
        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "auth.logout.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }
}
