package dev.springochyo.ortensia.ruleset.core;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {
    private final String rulesetId;
    private final List<StatType> stats;

    public RuleSet(String id) {
        this.rulesetId = id;
        stats = new ArrayList<>();
    }

    public RuleSet(String id, List<StatType> stats) {
        this.rulesetId = id;
        this.stats = stats;
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public List<StatType> getStats() {
        return stats;
    }
}
