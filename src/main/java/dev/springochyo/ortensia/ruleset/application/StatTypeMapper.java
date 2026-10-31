package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.application.dto.StatTypeDto;
import dev.springochyo.ortensia.ruleset.core.StatType;

public class StatTypeMapper {
    private StatTypeMapper() {}

    public static StatTypeDto toDto(StatType obj) {
        return new StatTypeDto(
                obj.getName(),
                obj.getDescription(),
                obj.getMinValueFormula(),
                obj.getMaxValueFormula(),
                obj.getMinValue(),
                obj.getMaxValue(),
                obj.getDependencies(),
                obj.isComputable()
        );
    }

    public static StatType toObj(StatTypeDto dto) {
        if (dto.computable())
            return new StatType(
                    dto.name(),
                    dto.description(),
                    dto.minValueFormula(),
                    dto.maxValueFormula(),
                    dto.dependencies()
            );

        return new StatType(
                dto.name(),
                dto.description(),
                dto.minValue(),
                dto.maxValue()
        );
    }
}
