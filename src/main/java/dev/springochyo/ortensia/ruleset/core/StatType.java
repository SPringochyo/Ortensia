package dev.springochyo.ortensia.ruleset.core;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.HashSet;
import java.util.List;

public class StatType{
    private final String name;
    private final String description;
    private final String minValueFormula;
    private final String maxValueFormula;
    private final Expression minExp;
    private final Expression maxExp;
    private final int minValue;
    private final int maxValue;
    private final int defaultValue;
    private final List<String> dependencies;
    private final boolean computable;
    private final ComputeStrategy computeStrategy;

    public StatType(String name, String description, int minValue, int maxValue, int defaultValue) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.minValueFormula = null;
        this.maxValueFormula = null;
        this.minExp = null;
        this.maxExp = null;
        this.dependencies = null;
        this.computable = false;
        this.computeStrategy = ComputeStrategy.DEFAULT;
    }

    public StatType(String name, String description, String minValueFormula, String maxValueFormula, int defaultValue, List<String> dependencies, ComputeStrategy computeStrategy) {
        this.name = name;
        this.description = description;
        this.minValueFormula = minValueFormula;
        this.maxValueFormula = maxValueFormula;
        this.minExp = new ExpressionBuilder(minValueFormula)
                .variables(new HashSet<>(dependencies)).build();
        this.maxExp = new ExpressionBuilder(maxValueFormula)
                .variables(new HashSet<>(dependencies)).build();
        this.defaultValue = defaultValue;
        this.dependencies = dependencies;
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
        this.computable = true;
        this.computeStrategy = computeStrategy;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMinValueFormula() {
        return minValueFormula;
    }

    public String getMaxValueFormula() {
        return maxValueFormula;
    }

    public Expression getMinExp() {
        return new Expression(minExp);
    }

    public Expression getMaxExp() {
        return new Expression(maxExp);
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public boolean isComputable() {
        return computable;
    }

    public ComputeStrategy getComputeStrategy() {
        return computeStrategy;
    }
}
