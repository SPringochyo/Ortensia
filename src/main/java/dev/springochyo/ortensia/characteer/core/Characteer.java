package dev.springochyo.ortensia.characteer.core;

import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

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

    public void changeStatByName(String name, short diff) {
        stats.get(name).changeStat(diff);
        recomputeStats();
    }

    private short recomputeStat(ComputeStrategy cs, short cur, short oldMin, short oldMax, short newMin, short newMax) {
        cur = switch(cs) {
            case DEFAULT -> cur;
            case UP -> (short) Math.max(cur, cur + (newMax - oldMax));
            case UPDOWN -> (short) (cur + (newMax - oldMax));
        };

        return cur;
    }

    public void recomputeStats() {
        for (String key : depQueue) {
            Stat stat = stats.get(key);
            Expression minf = new ExpressionBuilder(stat.getType().getMinValueFormula())
                    .variables(new HashSet<>(stat.getType().getDependencies())).build();
            Expression maxf = new ExpressionBuilder(stat.getType().getMaxValueFormula())
                    .variables(new HashSet<>(stat.getType().getDependencies())).build();
            for (String depKey : stat.getType().getDependencies()) {
                minf.setVariable(depKey, stats.get(depKey).getCurrentValue());
                maxf.setVariable(depKey, stats.get(depKey).getCurrentValue());
            }

            short newMin = (short) minf.evaluate();
            short newMax = (short) maxf.evaluate();
            short newCur = recomputeStat(
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

    public short getStatByName(String name) {
        return stats.get(name).getCurrentValue();
    }

    public Map<String, Stat> getStats() {
        return stats;
    }
}
