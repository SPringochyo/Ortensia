package dev.springochyo.ortensia.characteer.core;

import java.util.HashMap;
import java.util.Map;

public class Characteer {
    private final String rulesetId;
    private final String name;
    private final String description;
    private final Map<String, Stat> stats;

    public Characteer(String rulesetId, String name, String description) {
        this.rulesetId = rulesetId;
        this.name = name;
        this.description = description;
        stats = new HashMap<>();
    }

    public Characteer(String rulesetId, String name, String description, Map<String, Stat> stats) {
        this.rulesetId = rulesetId;
        this.name = name;
        this.description = description;
        this.stats = stats;
    }

    public void changeStatByName(String name, short diff) {
        stats.get(name).changeStat(diff);
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public short getStatByName(String name) {
        return stats.get(name).getCurrentValue();
    }

    public Map<String, Stat> getStats() {
        return stats;
    }
}
