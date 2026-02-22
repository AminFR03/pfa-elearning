package tn.enis.pfa.dto;

import tn.enis.pfa.entity.Enrollment;

import java.time.Instant;

public record EnrollmentDto(
        Long id,
        Long userId,
        Long courseId,
        String courseTitle,
        Instant enrolledAt,
        Boolean completed
) {
    public static EnrollmentDto from(Enrollment e) {
        return new EnrollmentDto(
                e.getId(),
                e.getUser().getId(),
                e.getCourse().getId(),
                e.getCourse().getTitle(),
                e.getEnrolledAt(),
                e.getCompleted()
        );
    }
}
