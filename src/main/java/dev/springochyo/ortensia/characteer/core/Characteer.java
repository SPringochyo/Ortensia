package dev.springochyo.ortensia.characteer.core;

import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;
import net.objecthunter.exp4j.Expression;

import java.util.*;

public class Characteer {
    private final String rulesetId;
    private final String name;
    private final String description;
    private final Map<String, Stat> stats;
    private final List<String> depQueue;

    public Characteer(String rulesetId, String name, String description, List<String> depQueue) {
        this.rulesetId = rulesetId;
        this.name = name;
        this.description = description;
        stats = new HashMap<>();
        this.depQueue = depQueue;
    }

    public Characteer(String rulesetId, String name, String description, List<String> depQueue, Map<String, Stat> stats) {
        this.rulesetId = rulesetId;
        this.name = name;
        this.description = description;
        this.stats = stats;
        this.depQueue = depQueue;
    }

    public void changeStatByName(String name, int diff) {
        stats.get(name).changeStat(diff);
        recomputeStats();
    }

    public void initializeStats() {
        for (String key : depQueue) {
            Stat stat = stats.get(key);
            Expression minf = stat.getType().getMinExp();
            Expression maxf = stat.getType().getMaxExp();
            for (String depKey : stat.getType().getDependencies()) {
                minf.setVariable(depKey, stats.get(depKey).getCurrentValue());
                maxf.setVariable(depKey, stats.get(depKey).getCurrentValue());
            }
            stat.setMinComputableValue((int) minf.evaluate());
            stat.setMaxComputableValue((int) maxf.evaluate());
        }
    }

    private int recomputeStat(ComputeStrategy cs, int cur, int oldMin, int oldMax, int newMin, int newMax) {
        return switch(cs) {
            case DEFAULT -> cur;
            case UP -> Math.max(cur, cur + (newMax - oldMax));
            case UPDOWN -> cur + (newMax - oldMax);
        };
    }

    public void recomputeStats() {
        for (String key : depQueue) {
            Stat stat = stats.get(key);
            Expression minf = stat.getType().getMinExp();
            Expression maxf = stat.getType().getMaxExp();
            for (String depKey : stat.getType().getDependencies()) {
                minf.setVariable(depKey, stats.get(depKey).getCurrentValue());
                maxf.setVariable(depKey, stats.get(depKey).getCurrentValue());
            }
            int newMin = (int) minf.evaluate();
            int newMax = (int) maxf.evaluate();
            int newCur = recomputeStat(
                    stat.getType().getComputeStrategy(),
                    stat.getCurrentValue(),
                    stat.getMinValue(),
                    stat.getMaxValue(),
                    newMin,
                    newMax
            );
            stat.setMinComputableValue(newMin);
            stat.setMaxComputableValue(newMax);
            stat.setCurrentValue(newCur);
        }
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

    public List<String> getDepQueue() {
        return depQueue;
    }

    public Map<String, Stat> getStats() {
        return stats;
    }
}
