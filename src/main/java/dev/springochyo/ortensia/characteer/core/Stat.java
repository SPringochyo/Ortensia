package dev.springochyo.ortensia.characteer.core;

import dev.springochyo.ortensia.ruleset.core.StatType;

public class Stat {
    private final StatType type;
    private short currentValue;
    private short minComputableValue;
    private short maxComputableValue;

    public Stat(StatType type) {
        this.type = type;
        this.currentValue = type.getDefaultValue();
    }

//  хз понадобится или нет, пока пусть висит
//    public short clamp(short value) {
//        if (!type.isComputable()) {
//            value = type.clamp(value);
//            return value;
//        }
//        value = (short) Math.min(value, maxComputableValue);
//        value = (short) Math.max(minComputableValue, value);
//        return value;
//    }

    public void changeStat(short diff) {
        if (!type.isComputable())
            currentValue = type.clamp((short) (currentValue + diff));
        else {
            currentValue = (short) Math.min(currentValue + diff, maxComputableValue);
            currentValue = (short) Math.max(minComputableValue, currentValue);
        }
    }

    public void setCurrentValue(short currentValue) {
        if (!type.isComputable())
            currentValue = type.clamp(currentValue);
        else {
            currentValue = (short) Math.min(currentValue, maxComputableValue);
            currentValue = (short) Math.max(minComputableValue, currentValue);
        }
        this.currentValue = currentValue;
    }

    public void setMinComputableValue(short minComputableValue) {
        this.minComputableValue = minComputableValue;
    }

    public void setMaxComputableValue(short maxComputableValue) {
        this.maxComputableValue = maxComputableValue;
    }

    public StatType getType() {
        return type;
    }

    public short getCurrentValue() {
        return currentValue;
    }

    public short getMinValue() {
        if (type.isComputable())
            return minComputableValue;
        return type.getMinValue();
    }

    public short getMaxValue() {
        if (type.isComputable())
            return maxComputableValue;
        return type.getMaxValue();
    }
}
