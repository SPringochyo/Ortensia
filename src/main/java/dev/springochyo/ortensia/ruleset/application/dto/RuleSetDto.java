package dev.springochyo.ortensia.ruleset.application.dto;

import java.util.List;

public record RuleSetDto(
        String rulesetId,
        List<StatTypeDto> stats
) {}
