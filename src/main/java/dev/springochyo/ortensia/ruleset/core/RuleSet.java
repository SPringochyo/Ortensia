package dev.springochyo.ortensia.ruleset.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleSet {
    private final String rulesetId;
    private final Map<String, StatType> stats;
    private final List<String> depQueue = new ArrayList<>();

    public RuleSet(String id) {
        this.rulesetId = id;
        stats = new HashMap<>();
    }

    public RuleSet(String id, Map<String, StatType> stats, List<String> depQueue) {
        this.rulesetId = id;
        this.stats = stats;
        this.depQueue.addAll(depQueue);
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public Map<String, StatType> getStats() {
        return stats;
    }

    public List<String> getDepQueue() {
        return depQueue;
    }
}
