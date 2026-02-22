package tn.enis.pfa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContentCreateRequest(
        @NotBlank String title,
        @NotBlank String type,
        String body,
        String videoUrl,
        @NotNull Integer orderIndex
) {}
