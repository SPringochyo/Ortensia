package dev.springochyo.ortensia.characteer.application;

import dev.springochyo.ortensia.characteer.application.dto.CharacteerDto;
import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.characteer.core.Stat;
import dev.springochyo.ortensia.ruleset.core.RuleSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacteerMapper {
    private CharacteerMapper() {}

    public static CharacteerDto toDto(Characteer obj) {
        Map<String, List<Integer>> stats = new HashMap<>();
        obj.getStats().forEach((key, value) -> {
            stats.put(key, List.of(value.getMinValue(), value.getMaxValue(), value.getCurrentValue()));
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

        for (String key : ruleSet.getStats().keySet()) {
            Stat stat = new Stat(ruleSet.getStats().get(key));
            if (stat.getType().isComputable()) {
                stat.setMinComputableValue(dto.stats().get(key).getFirst());
                stat.setMaxComputableValue(dto.stats().get(key).get(1));
                stat.setCurrentValue(dto.stats().get(key).getLast());
            } else {
                stat.setCurrentValue(dto.stats().get(key).getLast());
            }
            stats.put(key, stat);
        }

        return new Characteer(
                dto.rulesetId(),
                dto.name(),
                dto.description(),
                ruleSet.getDepQueue(),
                stats
        );
    }
}
