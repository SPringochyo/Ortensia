package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.core.StatType;

public class RuleSetCreator {
    private RuleSet ruleSet;

    public RuleSetCreator(String id) {
        this.ruleSet = new RuleSet(id);
    }

    public RuleSetCreator addStat(String name, String description, short minValue, short maxValue) {
        ruleSet.getStats().add(new StatType(name, description, minValue, maxValue));
        return this;
    }

    public RuleSet create() {
        return ruleSet;
    }

}
