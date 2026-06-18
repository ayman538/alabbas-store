package com.alabbas.store.controller;
import com.alabbas.store.service.AdminUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/setup")
public class SetupController {

    private final AdminUserService adminUserService;

    public SetupController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/admin")
    public String createAdmin(@RequestParam String username,
                              @RequestParam String password) {
        adminUserService.createAdmin(username, password);
        return "Admin created successfully";
    }
}