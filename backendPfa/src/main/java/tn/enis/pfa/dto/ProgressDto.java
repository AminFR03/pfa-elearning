package tn.enis.pfa.dto;

import tn.enis.pfa.entity.Progress;

import java.time.Instant;

public record ProgressDto(
        Long id,
        Long enrollmentId,
        Long contentId,
        Long exerciseId,
        Boolean completed,
        Double score,
        Instant completedAt
) {
    public static ProgressDto from(Progress p) {
        return new ProgressDto(
                p.getId(),
                p.getEnrollment().getId(),
                p.getContent() != null ? p.getContent().getId() : null,
                p.getExercise() != null ? p.getExercise().getId() : null,
                p.getCompleted(),
                p.getScore(),
                p.getCompletedAt()
        );
    }
}
