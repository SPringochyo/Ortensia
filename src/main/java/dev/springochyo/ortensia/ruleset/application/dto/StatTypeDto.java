package dev.springochyo.ortensia.ruleset.application.dto;

import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;

import java.util.List;

public record StatTypeDto(
        String name,
        String description,
        String minValueFormula,
        String maxValueFormula,
        short minValue,
        short maxValue,
        short defaultValue,
        List<String> dependencies,
        boolean computable,
        ComputeStrategy computeStrategy
) {}
