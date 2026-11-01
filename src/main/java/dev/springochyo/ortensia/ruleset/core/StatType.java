package dev.springochyo.ortensia.ruleset.core;

import java.util.List;

public class StatType{
    private final String name;
    private final String description;
    private final String minValueFormula;
    private final String maxValueFormula;
    private final short minValue;
    private final short maxValue;
    private final short defaultValue;
    private final List<String> dependencies;
    private final boolean computable;
    private final ComputeStrategy computeStrategy;

    public StatType(String name, String description, short minValue, short maxValue, short defaultValue) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.minValueFormula = null;
        this.maxValueFormula = null;
        this.dependencies = null;
        this.computable = false;
        this.computeStrategy = ComputeStrategy.DEFAULT;
    }

    public StatType(String name, String description, String minValueFormula, String maxValueFormula, short defaultValue, List<String> dependencies, ComputeStrategy computeStrategy) {
        this.name = name;
        this.description = description;
        this.minValueFormula = minValueFormula;
        this.maxValueFormula = maxValueFormula;
        this.defaultValue = defaultValue;
        this.dependencies = dependencies;
        this.minValue = Short.MIN_VALUE;
        this.maxValue = Short.MAX_VALUE;
        this.computable = true;
        this.computeStrategy = computeStrategy;
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

    public String getMinValueFormula() {
        return minValueFormula;
    }

    public String getMaxValueFormula() {
        return maxValueFormula;
    }

    public short getMinValue() {
        return minValue;
    }

    public short getMaxValue() {
        return maxValue;
    }

    public short getDefaultValue() {
        return defaultValue;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public boolean isComputable() {
        return computable;
    }

    public ComputeStrategy getComputeStrategy() {
        return computeStrategy;
    }
}
