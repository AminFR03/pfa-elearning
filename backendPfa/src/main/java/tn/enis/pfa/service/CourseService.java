package tn.enis.pfa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enis.pfa.dto.*;
import tn.enis.pfa.entity.Content;
import tn.enis.pfa.entity.Course;
import tn.enis.pfa.entity.CourseModule;
import tn.enis.pfa.entity.Exercise;
import tn.enis.pfa.entity.User;
import tn.enis.pfa.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final ContentRepository contentRepository;
    private final ExerciseRepository exerciseRepository;

    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream()
                .map(CourseDto::fromSummary)
                .toList();
    }

    public List<CourseDto> findByCategory(String category) {
        return courseRepository.findByCategory(category).stream()
                .map(CourseDto::fromSummary)
                .toList();
    }

    public List<CourseDto> findByTeacher(Long teacherId) {
        var teacher = userRepository.findById(teacherId).orElseThrow();
        return courseRepository.findByTeacher(teacher).stream()
                .map(CourseDto::fromSummary)
                .toList();
    }

    public CourseDto findById(Long id) {
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cours non trouvé"));
        return CourseDto.from(course);
    }

    @Transactional
    public CourseDto create(Long teacherId, CourseCreateRequest request) {
        var teacher = userRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé"));
        var course = Course.builder()
                .title(request.title())
                .description(request.description())
                .category(request.category())
                .teacher(teacher)
                .build();
        course = courseRepository.save(course);
        return CourseDto.fromSummary(course);
    }

    @Transactional
    public ModuleDto addModule(Long courseId, ModuleCreateRequest request) {
        var course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Cours non trouvé"));
        var module = CourseModule.builder()
                .title(request.title())
                .orderIndex(request.orderIndex())
                .course(course)
                .build();
        module = courseModuleRepository.save(module);
        return ModuleDto.from(module);
    }

    @Transactional
    public ContentDto addContent(Long moduleId, ContentCreateRequest request) {
        var module = courseModuleRepository.findById(moduleId).orElseThrow(() -> new IllegalArgumentException("Module non trouvé"));
        var type = Content.ContentType.valueOf(request.type().toUpperCase());
        var content = Content.builder()
                .title(request.title())
                .type(type)
                .body(request.body())
                .videoUrl(request.videoUrl())
                .orderIndex(request.orderIndex())
                .module(module)
                .build();
        content = contentRepository.save(content);
        return ContentDto.from(content);
    }

    @Transactional
    public ExerciseDto addExercise(Long moduleId, ExerciseCreateRequest request) {
        var module = courseModuleRepository.findById(moduleId).orElseThrow(() -> new IllegalArgumentException("Module non trouvé"));
        var type = Exercise.ExerciseType.valueOf(request.type().toUpperCase());
        var exercise = Exercise.builder()
                .question(request.question())
                .type(type)
                .correctAnswer(request.correctAnswer())
                .optionsJson(request.optionsJson())
                .orderIndex(request.orderIndex())
                .module(module)
                .build();
        exercise = exerciseRepository.save(exercise);
        return ExerciseDto.from(exercise);
    }
}
