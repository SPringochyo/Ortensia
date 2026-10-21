package dev.springochyo.ortensia.ruleset.application.port;

import dev.springochyo.ortensia.ruleset.core.RuleSet;

import java.util.List;

public interface RuleSetRepository {
    void save(RuleSet ruleSet);
    RuleSet load(String fileName);
    List<String> list();
}
