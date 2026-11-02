package dev.springochyo.ortensia.ruleset.application;

import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;
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

    public RuleSetCreator addStat(String name, String description, int minValue, int maxValue, int defaultValue) {
        ruleSet.getStats().put(name, new StatType(name, description, minValue, maxValue, defaultValue));
        return this;
    }

    public RuleSetCreator addStat(String name, String description, String minf, String maxf, int defaultValue, List<String> deps, ComputeStrategy cs) {
        ruleSet.getStats().put(name, new StatType(name, description, minf, maxf, defaultValue, deps, cs));
        return this;
    }

    private void createDepQueue() {
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

        List<String> dependencies = GraphUtils.kahnsAlgorithm(stats);

        ruleSet.getDepQueue().clear();
        ruleSet.getDepQueue().addAll(dependencies);
    }

    public RuleSet create() {
        createDepQueue();
        return ruleSet;
    }
}
