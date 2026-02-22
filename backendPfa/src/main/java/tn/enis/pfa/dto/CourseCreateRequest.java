package tn.enis.pfa.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseCreateRequest(
        @NotBlank String title,
        String description,
        @NotBlank String category
) {}
