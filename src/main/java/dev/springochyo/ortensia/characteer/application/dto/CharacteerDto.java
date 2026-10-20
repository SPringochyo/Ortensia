package dev.springochyo.ortensia.characteer.application.dto;

import java.util.Map;

public record CharacteerDto(
        String rulesetId,
        String name,
        String description,
        Map<String, Short>stats
) {}
