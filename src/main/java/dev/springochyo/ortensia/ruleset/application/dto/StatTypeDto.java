package dev.springochyo.ortensia.ruleset.application.dto;

import java.util.List;

public record StatTypeDto(
        String name,
        String description,
        String minValueFormula,
        String maxValueFormula,
        short minValue,
        short maxValue,
        List<String> dependencies,
        boolean computable
) {}
