package dev.springochyo.ortensia.characteer.application.port;

import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.ruleset.core.RuleSet;

import java.util.List;

public interface CharacteerRepository {
    void save(Characteer character);
    Characteer load(RuleSet ruleSet, String fileName);
    List<String> list();
}
