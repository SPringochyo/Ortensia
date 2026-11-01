package integration;

import dev.springochyo.ortensia.ruleset.application.RuleSetCreator;
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
                .addStat("крутость", "", (short) 0, (short) 20)
                .addStat("a", "", "", "", new ArrayList<>(List.of("крутость")))
                .addStat("b", "", "", "", new ArrayList<>(List.of("a", "крутость")))
                .addStat("c", "", "", "", new ArrayList<>(List.of("крутость", "крутость")))
                .addStat("d", "", "", "", new ArrayList<>(List.of("крутость", "b", "c")))
                .create();

        assertEquals(rule1.getDepQueue(), new ArrayList<>(List.of("a", "c", "b", "d")), "Invalid queue");
    }

    // Тест на независимые статы
    @Test
    void test2() {
        RuleSet rule1 = new RuleSetCreator("rule1")
                .addStat("крутость1", "", (short) 0, (short) 20)
                .addStat("крутость2", "", (short) 0, (short) 20)
                .addStat("крутость3", "", (short) 0, (short) 20)
                .create();

        assertEquals(rule1.getDepQueue(), new ArrayList<>(), "Invalid queue");
    }

    // Тест на циклическую зависимость
    @Test
    void test3() {
        var exception = assertThrows(CycleException.class, () -> {
            RuleSet rule1 = new RuleSetCreator("rule1")
                    .addStat("a", "", "", "", new ArrayList<>(List.of("b")))
                    .addStat("b", "", "", "", new ArrayList<>(List.of("c")))
                    .addStat("c", "", "", "", new ArrayList<>(List.of("a")))
                    .create();
        });

        assertEquals("Cyclic dependencies", exception.getMessage());
    }
}
