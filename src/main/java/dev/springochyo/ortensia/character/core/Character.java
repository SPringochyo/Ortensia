package dev.springochyo.ortensia.character.core;

import java.util.HashMap;
import java.util.Map;

public class Character {
    private final String rulesetId;
    private final String name;
    private final String description;
    private Map<String, Stat> stats = new HashMap<>();

    public Character(String rulesetId, String name, String description) {
        this.rulesetId = rulesetId;
        this.name = name;
        this.description = description;
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

    public Map<String, Stat> getStats() {
        return stats;
    }
}
