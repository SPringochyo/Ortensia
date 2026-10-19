package dev.springochyo.ortensia.characteer.application;

import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.characteer.core.Stat;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.core.StatType;

public class CharacteerCreator {
    private Characteer characteer;
    private RuleSet ruleSet;

    public CharacteerCreator(RuleSet ruleSet, String name, String description) {
        this.ruleSet = ruleSet;
        this.characteer = new Characteer(ruleSet.getRulesetId(), name, description);
        for (StatType type : ruleSet.getStats()) {
            characteer.getStats().put(type.getName(), new Stat(type));
        }
    }

    public Characteer create() {
        return characteer;
    }
}
