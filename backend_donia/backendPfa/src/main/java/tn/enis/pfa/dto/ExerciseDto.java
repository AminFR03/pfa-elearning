package tn.enis.pfa.dto;

import tn.enis.pfa.entity.Exercise;

public record ExerciseDto(
        Long id,
        String question,
        String type,
        String correctAnswer,
        String optionsJson,
        Integer orderIndex
) {
    public static ExerciseDto from(Exercise exercise) {
        return new ExerciseDto(
                exercise.getId(),
                exercise.getQuestion(),
                exercise.getType().name(),
                exercise.getCorrectAnswer(),
                exercise.getOptionsJson(),
                exercise.getOrderIndex()
        );
    }
}
