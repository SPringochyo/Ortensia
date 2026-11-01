package dev.springochyo.ortensia.characteer.application.dto;

import java.util.List;
import java.util.Map;

public record CharacteerDto(
        String rulesetId,
        String name,
        String description,
        List<String> depQueue,
        Map<String, Short> stats
) {}
