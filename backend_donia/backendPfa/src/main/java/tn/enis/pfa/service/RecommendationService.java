package tn.enis.pfa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tn.enis.pfa.dto.RecommendationDto;
import tn.enis.pfa.entity.*;
import tn.enis.pfa.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public List<RecommendationDto> getForUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        var stored = recommendationRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 20));
        if (!stored.isEmpty()) {
            return stored.stream().map(this::toDto).toList();
        }
        return computeRecommendations(userId);
    }

    private List<RecommendationDto> computeRecommendations(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        var result = new ArrayList<RecommendationDto>();
        var enrollments = enrollmentRepository.findByUser(user);
        var categories = enrollments.stream()
                .map(e -> e.getCourse().getCategory())
                .distinct()
                .toList();

        for (var category : categories) {
            var sameCategory = courseRepository.findByCategory(category);
            for (var c : sameCategory) {
                var alreadyEnrolled = enrollments.stream().anyMatch(e -> e.getCourse().getId().equals(c.getId()));
                if (!alreadyEnrolled) {
                    var r = Recommendation.builder()
                            .user(user)
                            .type(Recommendation.RecommendationType.COURSE)
                            .courseId(c.getId())
                            .reason("Basé sur votre intérêt pour la catégorie : " + category)
                            .score(0.8)
                            .build();
                    r = recommendationRepository.save(r);
                    result.add(RecommendationDto.from(r, c.getTitle()));
                    if (result.size() >= 5) break;
                }
            }
            if (result.size() >= 5) break;
        }

        if (result.size() < 3) {
            courseRepository.findAll().stream()
                    .filter(c -> enrollments.stream().noneMatch(e -> e.getCourse().getId().equals(c.getId())))
                    .limit(3 - result.size())
                    .forEach(c -> {
                        var r = Recommendation.builder()
                                .user(user)
                                .type(Recommendation.RecommendationType.COURSE)
                                .courseId(c.getId())
                                .reason("Cours populaire")
                                .score(0.5)
                                .build();
                        r = recommendationRepository.save(r);
                        result.add(RecommendationDto.from(r, c.getTitle()));
                    });
        }

        return result;
    }

    private RecommendationDto toDto(Recommendation r) {
        if (r.getCourseId() != null) {
            var courseTitle = courseRepository.findById(r.getCourseId())
                    .map(tn.enis.pfa.entity.Course::getTitle)
                    .orElse("");
            return RecommendationDto.from(r, courseTitle);
        }
        return RecommendationDto.from(r);
    }
}
