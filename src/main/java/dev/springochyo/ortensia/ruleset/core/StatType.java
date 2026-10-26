package dev.springochyo.ortensia.ruleset.core;

import java.util.List;

public class StatType{
    private final String name;
    private final String description;
    private final String minValueFormula;
    private final String maxValueFormula;
    private final short minValue;
    private final short maxValue;
    private final List<String> dependencies;
    private final boolean computable;

    public StatType(String name, String description, short minValue, short maxValue) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minValueFormula = null;
        this.maxValueFormula = null;
        this.dependencies = null;
        this.computable = false;
    }

    public StatType(String name, String description, String minValueFormula, String maxValueFormula, List<String> dependencies) {
        this.name = name;
        this.description = description;
        this.minValueFormula = minValueFormula;
        this.maxValueFormula = maxValueFormula;
        this.dependencies = dependencies;
        this.minValue = Short.MIN_VALUE;
        this.maxValue = Short.MAX_VALUE;
        this.computable = true;
    }

    public short clamp(short value) {
        value = (short) Math.min(value, maxValue);
        value = (short) Math.max(minValue, value);
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public short getMinValue() {
        return minValue;
    }

    public short getMaxValue() {
        return maxValue;
    }

    public String getMinValueFormula() {
        return minValueFormula;
    }

    public String getMaxValueFormula() {
        return maxValueFormula;
    }

    public boolean isComputable() {
        return computable;
    }
}
