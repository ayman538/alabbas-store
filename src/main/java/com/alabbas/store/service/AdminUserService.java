package com.alabbas.store.service;
import com.alabbas.store.entity.AdminUser;
import com.alabbas.store.enums.Role;
import com.alabbas.store.exception.BusinessException;
import com.alabbas.store.repository.AdminUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminUser createAdmin(String username, String password) {
        if (adminUserRepository.findByUsername(username).isPresent()) {
            throw new BusinessException("admin.username.exists");
        }

        AdminUser adminUser = AdminUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build();

        return adminUserRepository.save(adminUser);
    }
}