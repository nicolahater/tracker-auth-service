package dev.backend.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import dev.backend.authservice.dto.response.AuthResponse;
import dev.backend.authservice.dto.enumerates.NotificationType;
import dev.backend.authservice.dto.enumerates.RecipientType;
import dev.backend.authservice.dto.UserDto;
import dev.backend.authservice.dto.UserRegisteredPayload;
import dev.backend.authservice.dto.request.UserRequestDTO;
import dev.backend.authservice.entity.User;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userRequestDTO.getPassword()))")
    User toEntityWithPassword(UserRequestDTO userRequestDTO, PasswordEncoder passwordEncoder);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "templateType", expression = "java(getWelcomeTemplateType())")
    @Mapping(target = "recipientType", expression = "java(getEmailRecipientType())")
    @Mapping(target = "data", expression = "java(java.util.Collections.emptyMap())")
    UserRegisteredPayload toUserRegisteredPayload(User user);

    @Named("getWelcomeTemplateType")
    default String getWelcomeTemplateType() {
        return NotificationType.WELCOME.getTemplateName().toUpperCase();
    }

    @Named("getEmailRecipientType")
    default String getEmailRecipientType() {
        return RecipientType.EMAIL.getRecipientName();
    }

    @Mapping(target = "accessToken", source = "jwtToken")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.username")
    @Mapping(target = "email", source = "user.email", qualifiedByName = "getUserEmail")
    @Mapping(target = "expiresIn", constant = "3600")
    @Mapping(target = "role", constant = "ADMIN")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "department", ignore = true)
    AuthResponse toAuthResponse(String jwtToken, User user);

    @Named("getUserEmail")
    default String getUserEmail(String email) {
        return email != null ? email : "user@example.com";
    }
}