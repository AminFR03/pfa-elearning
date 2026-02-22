package tn.enis.pfa.dto;

import tn.enis.pfa.entity.Content;

public record ContentDto(
        Long id,
        String title,
        String type,
        String body,
        String videoUrl,
        Integer orderIndex
) {
    public static ContentDto from(Content content) {
        return new ContentDto(
                content.getId(),
                content.getTitle(),
                content.getType().name(),
                content.getBody(),
                content.getVideoUrl(),
                content.getOrderIndex()
        );
    }
}
