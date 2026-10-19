package dev.springochyo.ortensia.ruleset.core;

public class StatType{
    private final String name;
    private final String description;
    private short minValue;
    private short maxValue;

    public StatType(String name, String description, short minValue, short maxValue) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
}
