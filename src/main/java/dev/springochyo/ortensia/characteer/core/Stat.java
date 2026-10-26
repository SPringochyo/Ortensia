package dev.springochyo.ortensia.characteer.core;

import dev.springochyo.ortensia.ruleset.core.StatType;

public class Stat {
    private final StatType type;
    private short currentValue = 0;
    private short minComputableValue;
    private short maxComputableValue;

    public Stat(StatType type) {
        this.type = type;
    }

    public void changeStat(short diff) {
        if (!type.isComputable())
            currentValue = type.clamp((short) (currentValue + diff));
        else {
            currentValue = (short) Math.min(currentValue + diff, maxComputableValue);
            currentValue = (short) Math.max(minComputableValue, currentValue);
        }
    }

    public void setCurrentValue(short currentValue) {
        this.currentValue = currentValue;
    }

    public StatType getType() {
        return type;
    }

    public short getCurrentValue() {
        return currentValue;
    }
}
