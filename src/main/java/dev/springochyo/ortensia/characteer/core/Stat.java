package dev.springochyo.ortensia.characteer.core;

import dev.springochyo.ortensia.ruleset.core.StatType;

public class Stat {
    private final StatType type;
    private int currentValue;
    private int minComputableValue;
    private int maxComputableValue;

    public Stat(StatType type) {
        this.type = type;
        this.currentValue = type.getDefaultValue();
    }

    public int clamp(int value) {
        return Math.clamp(value, getMinValue(), getMaxValue());
    }

    public void changeStat(int diff) {
        currentValue = clamp(currentValue + diff);
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = clamp(currentValue);
    }

    public void setMinComputableValue(int minComputableValue) {
        this.minComputableValue = minComputableValue;
    }

    public void setMaxComputableValue(int maxComputableValue) {
        this.maxComputableValue = maxComputableValue;
    }

    public StatType getType() {
        return type;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public int getMinValue() {
        if (type.isComputable())
            return minComputableValue;
        return type.getMinValue();
    }

    public int getMaxValue() {
        if (type.isComputable())
            return maxComputableValue;
        return type.getMaxValue();
    }
}
