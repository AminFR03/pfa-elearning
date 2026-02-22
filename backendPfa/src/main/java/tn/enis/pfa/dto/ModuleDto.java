package tn.enis.pfa.dto;

import tn.enis.pfa.entity.CourseModule;

import java.util.Collections;
import java.util.List;

public record ModuleDto(
        Long id,
        String title,
        Integer orderIndex,
        List<ContentDto> contents,
        List<ExerciseDto> exercises
) {
    public static ModuleDto from(CourseModule module) {
        var contents = module.getContents() != null
                ? module.getContents().stream().map(ContentDto::from).toList()
                : Collections.<ContentDto>emptyList();
        var exercises = module.getExercises() != null
                ? module.getExercises().stream().map(ExerciseDto::from).toList()
                : Collections.<ExerciseDto>emptyList();
        return new ModuleDto(
                module.getId(),
                module.getTitle(),
                module.getOrderIndex(),
                contents,
                exercises
        );
    }
}
