package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.core.StatType;
import dev.springochyo.ortensia.utils.GraphUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleSetCreator {
    private RuleSet ruleSet;

    public RuleSetCreator(String id) {
        this.ruleSet = new RuleSet(id);
    }

    public RuleSetCreator addStat(String name, String description, short minValue, short maxValue) {
        ruleSet.getStats().put(name, new StatType(name, description, minValue, maxValue));
        return this;
    }

    public RuleSetCreator addStat(String name, String description, String minf, String maxf, List<String> deps) {
        ruleSet.getStats().put(name, new StatType(name, description, minf, maxf, deps));
        return this;
    }

    public RuleSetCreator createDepQueue() {
        Map<String, List<String>> stats = new HashMap<>();
        for (String key : ruleSet.getStats().keySet()) {
            if (ruleSet.getStats().get(key).isComputable()) {
                List<String> dependencies = new ArrayList<>();
                for (String name : ruleSet.getStats().get(key).getDependencies())
                    if (ruleSet.getStats().get(name).isComputable())
                        dependencies.add(name);
                stats.put(key, dependencies);
            }
        }

        List<String> dependencies = GraphUtils.KhansAlgorithm(stats);

        ruleSet.getDepQueue().clear();
        ruleSet.getDepQueue().addAll(dependencies);

        return this;
    }

    public RuleSet create() {
        return ruleSet;
    }
}
