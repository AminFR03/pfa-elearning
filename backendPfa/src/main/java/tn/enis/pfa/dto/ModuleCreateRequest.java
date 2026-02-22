package tn.enis.pfa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModuleCreateRequest(
        @NotBlank String title,
        @NotNull Integer orderIndex
) {}
