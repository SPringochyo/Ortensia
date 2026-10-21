package dev.springochyo.ortensia.characteer.core;

import dev.springochyo.ortensia.ruleset.core.StatType;

public class Stat {
    private StatType type;
    private short currentValue = 0;

    public Stat(StatType type) {
        this.type = type;
    }

    public void changeStat(short diff) {
        currentValue = type.clamp((short) (currentValue + diff));
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
