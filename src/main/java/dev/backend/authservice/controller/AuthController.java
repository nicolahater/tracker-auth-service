package dev.backend.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.backend.authservice.dto.response.AuthResponse;
import dev.backend.authservice.dto.request.LoginRequest;
import dev.backend.authservice.dto.UserDto;
import dev.backend.authservice.dto.request.UserRequestDTO;
import dev.backend.authservice.entity.User;
import dev.backend.authservice.service.UserService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public AuthResponse login(@Validated @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public User register(@Validated @RequestBody UserRequestDTO request) {
        return userService.signUp(request);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}