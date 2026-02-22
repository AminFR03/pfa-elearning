package tn.enis.pfa.dto;

import tn.enis.pfa.entity.User;

import java.time.Instant;

public record UserDto(
        Long id,
        String email,
        String fullName,
        String role,
        Instant createdAt
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
