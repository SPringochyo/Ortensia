package dev.springochyo.ortensia.characteer.application;

import dev.springochyo.ortensia.characteer.application.dto.CharacteerDto;
import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.characteer.core.Stat;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.core.StatType;

import java.util.HashMap;
import java.util.Map;

public class CharacteerMapper {
    private CharacteerMapper() {}

    public static CharacteerDto toDto(Characteer obj) {
        Map<String, Short> stats = new HashMap<>();
        obj.getStats().forEach((key, value) -> {
            stats.put(key, value.getCurrentValue());
        });
        return new CharacteerDto(
                obj.getRulesetId(),
                obj.getName(),
                obj.getDescription(),
                stats
        );
    }

    public static Characteer toObj(RuleSet ruleSet, CharacteerDto dto) {
        Map<String, Stat> stats = new HashMap<>();

        if (!ruleSet.getRulesetId().equals(dto.rulesetId()))
            throw new InvalidRuleSetException("Different ruleset id");

        for (StatType type : ruleSet.getStats()) {
            Stat stat = new Stat(type);
            stat.setCurrentValue(dto.stats().get(type.getName()));
            stats.put(type.getName(), stat);
        }

        return new Characteer(
                dto.rulesetId(),
                dto.name(),
                dto.description(),
                stats
        );
    }
}
