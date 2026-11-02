package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.application.dto.RuleSetDto;
import dev.springochyo.ortensia.ruleset.application.dto.StatTypeDto;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.core.StatType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleSetMapper {
    private RuleSetMapper() {}

    public static RuleSetDto toDto(RuleSet obj) {
        List<StatTypeDto> stats = new ArrayList<>();
        for (String key : obj.getStats().keySet())
            stats.add(StatTypeMapper.toDto(obj.getStats().get(key)));

        return new RuleSetDto(
                obj.getRulesetId(),
                stats,
                obj.getDepQueue()
        );
    }

    public static RuleSet toObj(RuleSetDto dto) {
        Map<String, StatType> stats = new HashMap<>();
        for (StatTypeDto type : dto.stats())
            stats.put(type.name(), StatTypeMapper.toObj(type));

        return new RuleSet(
                dto.rulesetId(),
                stats,
                dto.depQueue()
        );
    }
}
