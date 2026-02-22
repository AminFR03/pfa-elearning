package tn.enis.pfa.dto;

import tn.enis.pfa.entity.Course;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public record CourseDto(
        Long id,
        String title,
        String description,
        String category,
        Long teacherId,
        String teacherName,
        Instant createdAt,
        List<ModuleDto> modules,
        Integer enrollmentCount
) {
    public static CourseDto from(Course course) {
        var modules = course.getModules() != null
                ? course.getModules().stream().map(ModuleDto::from).toList()
                : Collections.<ModuleDto>emptyList();
        var enrollmentCount = course.getEnrollments() != null ? course.getEnrollments().size() : 0;
        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getTeacher().getId(),
                course.getTeacher().getFullName(),
                course.getCreatedAt(),
                modules,
                enrollmentCount
        );
    }

    public static CourseDto fromSummary(Course course) {
        var enrollmentCount = course.getEnrollments() != null ? course.getEnrollments().size() : 0;
        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getTeacher().getId(),
                course.getTeacher().getFullName(),
                course.getCreatedAt(),
                null,
                enrollmentCount
        );
    }
}
