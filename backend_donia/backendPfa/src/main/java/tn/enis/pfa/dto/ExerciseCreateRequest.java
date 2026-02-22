package tn.enis.pfa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExerciseCreateRequest(
        @NotBlank String question,
        @NotBlank String type,
        String correctAnswer,
        String optionsJson,
        @NotNull Integer orderIndex
) {}
