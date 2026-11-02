package integration;

import dev.springochyo.ortensia.ruleset.application.RuleSetCreator;
import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.utils.CycleException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepQueue {
    // Тест на среднюю ситуацию зависимостей
    @Test
    void test1() {
        RuleSet rule1 = new RuleSetCreator("rule1")
                .addStat("крутость", "", 0, 20, 10)
                .addStat("a", "", "0", "0", 10, new ArrayList<>(List.of("крутость")), ComputeStrategy.DEFAULT)
                .addStat("b", "", "0", "0", 10, new ArrayList<>(List.of("a", "крутость")), ComputeStrategy.DEFAULT)
                .addStat("c", "", "0", "0", 10, new ArrayList<>(List.of("крутость", "крутость")), ComputeStrategy.DEFAULT)
                .addStat("d", "", "0", "0", 10, new ArrayList<>(List.of("крутость", "b", "c")), ComputeStrategy.DEFAULT)
                .create();

        assertEquals(new ArrayList<>(List.of("a", "c", "b", "d")), rule1.getDepQueue(), "Invalid queue");
    }

    // Тест на независимые статы
    @Test
    void test2() {
        RuleSet rule1 = new RuleSetCreator("rule1")
                .addStat("крутость1", "", 0, 20, 10)
                .addStat("крутость2", "", 0, 20, 10)
                .addStat("крутость3", "", 0, 20,10)
                .create();

        assertEquals(new ArrayList<>(), rule1.getDepQueue(), "Invalid queue");
    }

    // Тест на циклическую зависимость
    @Test
    void test3() {
        var exception = assertThrows(CycleException.class, () -> {
            RuleSet rule1 = new RuleSetCreator("rule1")
                    .addStat("a", "", "0", "0", 10, new ArrayList<>(List.of("b")), ComputeStrategy.DEFAULT)
                    .addStat("b", "", "0", "0", 10, new ArrayList<>(List.of("c")), ComputeStrategy.DEFAULT)
                    .addStat("c", "", "0", "0", 10, new ArrayList<>(List.of("a")), ComputeStrategy.DEFAULT)
                    .create();
        });

        assertEquals("Cyclic dependencies", exception.getMessage());
    }
}
