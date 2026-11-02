package integration;

import dev.springochyo.ortensia.characteer.application.CharacteerCreator;
import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.characteer.core.Stat;
import dev.springochyo.ortensia.ruleset.application.RuleSetCreator;
import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatComputeStrategyTest {

    @Test
    @DisplayName("DEFAULT: текущее значение не растет при увеличении максимума, но зажимается при уменьшении")
    void testDefaultStrategy() {
        RuleSet ruleSet = new RuleSetCreator("rules")
                .addStat("тело", "", 0, 20, 10)
                .addStat("мана", "", "0", "тело", 10, List.of("тело"), ComputeStrategy.DEFAULT)
                .create();

        Characteer hero = new CharacteerCreator(ruleSet, "Mage", "").create();
        Stat mana = hero.getStats().get("мана");

        // Исходно: тело = 10 -> мана = 10/10
        assertEquals(10, mana.getCurrentValue());
        assertEquals(10, mana.getMaxValue());

        // Тратим ману до 8
        hero.changeStatByName("мана", -2);
        assertEquals(8, mana.getCurrentValue());

        // Бафф тела (+5): тело = 15, максимум маны растет (10 -> 15), текущая мана остается 8
        hero.changeStatByName("тело", 5);
        assertEquals(15, mana.getMaxValue());
        assertEquals(8, mana.getCurrentValue());

        // Снижаем тело (-10): тело становится 5 (в пределах [0, 20]!).
        // Максимум падает до 5. Текущая мана (была 8) обязана зажаться до нового максимума (5)
        hero.changeStatByName("тело", -10);
        assertEquals(5, mana.getMaxValue());
        assertEquals(5, mana.getCurrentValue());
    }

    @Test
    @DisplayName("UP: текущее значение растет вместе с максимумом, но не падает при его уменьшении (если не зажато)")
    void testUpStrategy() {
        RuleSet ruleSet = new RuleSetCreator("rules")
                .addStat("тело", "", 0, 20, 4)
                .addStat("хп", "", "0", "12 + тело", 16, List.of("тело"), ComputeStrategy.UP)
                .create();

        Characteer hero = new CharacteerCreator(ruleSet, "Warrior", "").create();
        Stat hp = hero.getStats().get("хп");

        // Исходное состояние: тело = 4 -> хп = 16/16
        assertEquals(16, hp.getCurrentValue());
        assertEquals(16, hp.getMaxValue());

        // Наносим персонажу урон (хп = 12/16)
        hero.changeStatByName("хп", -4);
        assertEquals(12, hp.getCurrentValue());

        // Увеличиваем тело (+2): максимум растет (16 -> 18), текущее хп растет на ту же дельту (12 -> 14)
        hero.changeStatByName("тело", 2);
        assertEquals(18, hp.getMaxValue());
        assertEquals(14, hp.getCurrentValue());

        // Уменьшаем тело (-1): максимум падает (18 -> 17), но текущее хп НЕ уменьшается (остается 14)
        hero.changeStatByName("тело", -1);
        assertEquals(17, hp.getMaxValue());
        assertEquals(14, hp.getCurrentValue());

        // Снижаем тело сильно (-4): максимум падает ниже текущего (17 -> 13). ХП зажимается до 13
        hero.changeStatByName("тело", -4);
        assertEquals(13, hp.getMaxValue());
        assertEquals(13, hp.getCurrentValue());
    }

    @Test
    @DisplayName("UPDOWN: текущее значение синхронно увеличивается и уменьшается вслед за максимумом")
    void testUpDownStrategy() {
        RuleSet ruleSet = new RuleSetCreator("rules")
                .addStat("сила", "", 0, 20, 10)
                .addStat("перенос", "", "0", "сила * 5", 50, List.of("сила"), ComputeStrategy.UPDOWN)
                .create();

        Characteer hero = new CharacteerCreator(ruleSet, "Rogue", "").create();
        Stat carry = hero.getStats().get("перенос");

        // Исходно: сила = 10 -> перенос = 50/50
        assertEquals(50, carry.getCurrentValue());
        assertEquals(50, carry.getMaxValue());

        // Частично снижаем текущий ресурс до 40
        hero.changeStatByName("перенос", -10);
        assertEquals(40, carry.getCurrentValue());

        // Бафф силы (+2): максимум 50 -> 60, текущее синхронно растет 40 -> 50
        hero.changeStatByName("сила", 2);
        assertEquals(60, carry.getMaxValue());
        assertEquals(50, carry.getCurrentValue());

        // Дебафф силы (-4): максимум 60 -> 40, текущее синхронно падает 50 -> 30
        hero.changeStatByName("сила", -4);
        assertEquals(40, carry.getMaxValue());
        assertEquals(30, carry.getCurrentValue());
    }

    @Test
    @DisplayName("Каскадный пересчет: цепочка зависимостей А -> Б -> В пересчитывается корректно")
    void testChainedDependenciesRecalculation() {
        RuleSet ruleSet = new RuleSetCreator("rules")
                .addStat("тело", "", 0, 20, 2)
                .addStat("базовое_хп", "", "0", "тело * 5", 10, List.of("тело"), ComputeStrategy.UPDOWN)
                .addStat("итоговое_хп", "", "0", "базовое_хп + 10", 20, List.of("базовое_хп"), ComputeStrategy.UPDOWN)
                .create();

        Characteer hero = new CharacteerCreator(ruleSet, "Tank", "").create();
        Stat finalHp = hero.getStats().get("итоговое_хп");

        // Начало: тело = 2 -> базовое_хп = 10 -> итоговое_хп = 20
        assertEquals(20, finalHp.getCurrentValue());
        assertEquals(20, finalHp.getMaxValue());

        // Меняем корень дерева зависимостей (+2 к телу)
        // тело: 4 -> базовое_хп max: 20 (+10) -> итоговое_хп max: 30 (+10)
        hero.changeStatByName("тело", 2);
        assertEquals(30, finalHp.getMaxValue());
        assertEquals(30, finalHp.getCurrentValue());
    }
}