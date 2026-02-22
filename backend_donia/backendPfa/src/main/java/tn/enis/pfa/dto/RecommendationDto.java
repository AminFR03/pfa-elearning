package tn.enis.pfa.dto;

import tn.enis.pfa.entity.Recommendation;

import java.time.Instant;

public record RecommendationDto(
        Long id,
        String type,
        Long courseId,
        String courseTitle,
        Long contentId,
        Long moduleId,
        String reason,
        Double score,
        Instant createdAt
) {
    public static RecommendationDto from(Recommendation r) {
        return from(r, null);
    }

    public static RecommendationDto from(Recommendation r, String courseTitle) {
        return new RecommendationDto(
                r.getId(),
                r.getType().name(),
                r.getCourseId(),
                courseTitle != null ? courseTitle : "",
                r.getContentId(),
                r.getModuleId(),
                r.getReason(),
                r.getScore(),
                r.getCreatedAt()
        );
    }
}
