package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.application.dto.RuleSetDto;
import dev.springochyo.ortensia.ruleset.application.dto.StatTypeDto;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.core.StatType;

import java.util.ArrayList;
import java.util.List;

public class RuleSetMapper {
    private RuleSetMapper() {}

    public static RuleSetDto toDto(RuleSet obj) {
        List<StatTypeDto> stats = new ArrayList<>();
        for (StatType type : obj.getStats())
            stats.add(StatTypeMapper.toDto(type));

        return new RuleSetDto(
                obj.getRulesetId(),
                stats
        );
    }

    public static RuleSet toObj(RuleSetDto dto) {
        List<StatType> stats = new ArrayList<>();
        for (StatTypeDto type : dto.stats())
            stats.add(StatTypeMapper.toObj(type));

        return new RuleSet(
                dto.rulesetId(),
                stats
        );
    }
}
