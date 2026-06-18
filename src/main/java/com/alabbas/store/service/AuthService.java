package com.alabbas.store.service;

import com.alabbas.store.dto.LoginRequest;
import com.alabbas.store.dto.LoginResponse;
import com.alabbas.store.entity.AdminUser;
import com.alabbas.store.exception.BusinessException;
import com.alabbas.store.repository.AdminUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RevokedTokenService revokedTokenService;

    public AuthService(AdminUserRepository adminUserRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RevokedTokenService revokedTokenService) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.revokedTokenService = revokedTokenService;
    }

    public LoginResponse login(LoginRequest request) {

        AdminUser adminUser = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("auth.invalid.credentials"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                adminUser.getPassword()
        );

        if (!passwordMatches) {
            throw new BusinessException("auth.invalid.credentials");
        }

        String token = jwtService.generateToken(adminUser.getUsername());

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    public void logout(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        revokedTokenService.revoke(token, jwtService.extractExpiration(token).toInstant());
        SecurityContextHolder.clearContext();
    }
}
