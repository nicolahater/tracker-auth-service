package dev.backend.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.backend.authservice.dto.response.AuthResponse;
import dev.backend.authservice.dto.enumerates.EventType;
import dev.backend.authservice.dto.request.LoginRequest;
import dev.backend.authservice.dto.UserDto;
import dev.backend.authservice.dto.UserRegisteredPayload;
import dev.backend.authservice.dto.request.UserRequestDTO;
import dev.backend.authservice.entity.User;
import dev.backend.authservice.mapper.UserMapper;
import dev.backend.authservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final OutboxService outboxService;

    private final UserMapper userMapper;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        String jwtToken = jwtTokenService.generateJwtToken(authentication);

        User user = userRepository.findUserByUsername(request.username())
            .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toAuthResponse(jwtToken, user);
    }

    @Transactional
    public User signUp(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntityWithPassword(userRequestDTO, passwordEncoder);
        User savedUser = userRepository.save(user);

        UserRegisteredPayload payload = userMapper.toUserRegisteredPayload(savedUser);
        outboxService.createEvent(EventType.USER_REGISTERED.getEventTypeName(), payload);

        return savedUser;
    }

    public List<UserDto> getAllUsers() {
        return Streamable.of(userRepository.findAll()).stream()
            .map(userMapper::toUserDto)
            .toList();
    }
}