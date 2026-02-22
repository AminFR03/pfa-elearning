package tn.enis.pfa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enis.pfa.dto.ProgressDto;
import tn.enis.pfa.dto.ProgressRequest;
import tn.enis.pfa.entity.*;
import tn.enis.pfa.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ContentRepository contentRepository;
    private final ExerciseRepository exerciseRepository;

    public List<ProgressDto> findByEnrollment(Long enrollmentId) {
        return progressRepository.findByEnrollmentId(enrollmentId).stream()
                .map(ProgressDto::from)
                .toList();
    }

    @Transactional
    public ProgressDto recordProgress(Long userId, Long enrollmentId, ProgressRequest request) {
        var enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Inscription non trouvée"));
        if (!enrollment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Non autorisé");
        }
        var completed = request.completed() != null ? request.completed() : true;
        var progress = switch (request) {
            case ProgressRequest(Long contentId, Long exId, Double sc, Boolean comp) when contentId != null -> {
                var content = contentRepository.findById(contentId).orElseThrow();
                var existing = progressRepository.findByEnrollmentIdAndContentId(enrollmentId, contentId);
                if (existing.isPresent()) {
                    var p = existing.get();
                    p.setCompleted(completed);
                    yield p;
                }
                yield progressRepository.save(Progress.builder()
                        .enrollment(enrollment)
                        .content(content)
                        .completed(completed)
                        .build());
            }
            case ProgressRequest(Long cId, Long exerciseId, Double score, Boolean comp) when exerciseId != null -> {
                var exercise = exerciseRepository.findById(exerciseId).orElseThrow();
                var existing = progressRepository.findByEnrollmentIdAndExerciseId(enrollmentId, exerciseId);
                if (existing.isPresent()) {
                    var p = existing.get();
                    p.setScore(score);
                    p.setCompleted(completed);
                    yield p;
                }
                yield progressRepository.save(Progress.builder()
                        .enrollment(enrollment)
                        .exercise(exercise)
                        .score(score)
                        .completed(completed)
                        .build());
            }
            default -> throw new IllegalArgumentException("contentId ou exerciseId requis");
        };
        return ProgressDto.from(progress);
    }
}
