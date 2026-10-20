package dev.springochyo.ortensia.ruleset.application.dto;

public record StatTypeDto(
        String name,
        String description,
        short minValue,
        short maxValue
) {}
