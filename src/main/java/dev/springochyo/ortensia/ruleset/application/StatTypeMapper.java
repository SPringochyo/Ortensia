package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.application.dto.StatTypeDto;
import dev.springochyo.ortensia.ruleset.core.StatType;

public class StatTypeMapper {
    private StatTypeMapper() {}

    public static StatTypeDto toDto(StatType obj) {
        return new StatTypeDto(
                obj.getName(),
                obj.getDescription(),
                obj.getMinValue(),
                obj.getMaxValue()
        );
    }

    public static StatType toObj(StatTypeDto dto) {
        return new StatType(
                dto.name(),
                dto.description(),
                dto.minValue(),
                dto.maxValue()
        );
    }
}
