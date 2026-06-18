package com.alabbas.store.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alabbas.store.entity.AdminUser;
import java.util.Optional;


public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);
}