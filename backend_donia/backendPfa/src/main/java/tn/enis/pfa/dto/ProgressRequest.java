package tn.enis.pfa.dto;

public record ProgressRequest(
        Long contentId,
        Long exerciseId,
        Double score,
        Boolean completed
) {}
