package dev.springochyo.ortensia.ruleset.core;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {
    private final String rulesetId;
    private final List<StatType> stats = new ArrayList<>();

    public RuleSet(String id) {
        this.rulesetId = id;
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public List<StatType> getStats() {
        return stats;
    }
}
