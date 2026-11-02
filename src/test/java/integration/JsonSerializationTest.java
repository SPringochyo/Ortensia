package integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.springochyo.ortensia.characteer.application.CharacteerCreator;
import dev.springochyo.ortensia.characteer.application.CharacteerMapper;
import dev.springochyo.ortensia.characteer.application.InvalidRuleSetException;
import dev.springochyo.ortensia.characteer.application.dto.CharacteerDto;
import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.characteer.core.Stat;
import dev.springochyo.ortensia.ruleset.application.RuleSetCreator;
import dev.springochyo.ortensia.ruleset.application.RuleSetMapper;
import dev.springochyo.ortensia.ruleset.application.dto.RuleSetDto;
import dev.springochyo.ortensia.ruleset.core.ComputeStrategy;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSerializationTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test
    @DisplayName("RuleSet: корректный экспорт в JSON и импорт с сохранением структуры и depQueue")
    void testRuleSetJsonRoundTrip() {
        RuleSet originalRuleSet = new RuleSetCreator("dnd5e_custom")
                .addStat("тело", "Выносливость", 0, 20, 10)
                .addStat("хп", "Здоровье", "0", "10 + тело", 20, List.of("тело"), ComputeStrategy.UP)
                .addStat("мана", "Энергия", "0", "хп * 2", 40, List.of("хп"), ComputeStrategy.UPDOWN)
                .create();

        // 1. Экспорт RuleSet -> DTO -> JSON
        RuleSetDto exportDto = RuleSetMapper.toDto(originalRuleSet);
        String json = gson.toJson(exportDto);

        assertNotNull(json);
        assertFalse(json.isBlank());

        // 2. Импорт JSON -> DTO -> RuleSet
        RuleSetDto importedDto = gson.fromJson(json, RuleSetDto.class);
        RuleSet restoredRuleSet = RuleSetMapper.toObj(importedDto);

        // 3. Проверка целостности
        assertEquals(originalRuleSet.getRulesetId(), restoredRuleSet.getRulesetId());
        assertEquals(originalRuleSet.getDepQueue(), restoredRuleSet.getDepQueue());
        assertEquals(originalRuleSet.getStats().keySet(), restoredRuleSet.getStats().keySet());

        var hpType = restoredRuleSet.getStats().get("хп");
        assertTrue(hpType.isComputable());
        assertEquals("10 + тело", hpType.getMaxValueFormula());
        assertEquals(ComputeStrategy.UP, hpType.getComputeStrategy());
        assertEquals(List.of("тело"), hpType.getDependencies());
    }

    @Test
    @DisplayName("Characteer: корректный экспорт и импорт со снапшотом [min, max, cur]")
    void testCharacteerJsonRoundTrip() {
        RuleSet ruleSet = new RuleSetCreator("classic_rules")
                .addStat("сила", "", 0, 20, 10)
                .addStat("тело", "", 0, 20, 10)
                .addStat("хп", "", "0", "тело * 2", 20, List.of("тело"), ComputeStrategy.UP)
                .create();

        Characteer originalChar = new CharacteerCreator(ruleSet, "Warrior", "Tank character").create();

        // Модифицируем характеристики перед экспортом
        originalChar.changeStatByName("тело", 2); // тело = 12, max хп = 24, cur хп = 24
        originalChar.changeStatByName("хп", -5);   // cur хп = 19 (ранен)
        originalChar.changeStatByName("сила", 3); // сила = 13

        // 1. Экспорт: Characteer -> DTO -> JSON
        CharacteerDto exportDto = CharacteerMapper.toDto(originalChar);
        String json = gson.toJson(exportDto);

        assertNotNull(json);

        // 2. Импорт: JSON -> DTO -> Characteer
        CharacteerDto importedDto = gson.fromJson(json, CharacteerDto.class);
        Characteer restoredChar = CharacteerMapper.toObj(ruleSet, importedDto);

        // 3. Проверка базовых полей
        assertEquals(originalChar.getRulesetId(), restoredChar.getRulesetId());
        assertEquals(originalChar.getName(), restoredChar.getName());
        assertEquals(originalChar.getDescription(), restoredChar.getDescription());
        assertEquals(originalChar.getDepQueue(), restoredChar.getDepQueue());

        // 4. Проверка значений обычных и вычисляемых статов
        Stat restoredBody = restoredChar.getStats().get("тело");
        assertEquals(0, restoredBody.getMinValue());
        assertEquals(20, restoredBody.getMaxValue());
        assertEquals(12, restoredBody.getCurrentValue());

        Stat restoredStrength = restoredChar.getStats().get("сила");
        assertEquals(13, restoredStrength.getCurrentValue());

        Stat restoredHp = restoredChar.getStats().get("хп");
        assertEquals(0, restoredHp.getMinValue());
        assertEquals(24, restoredHp.getMaxValue());
        assertEquals(19, restoredHp.getCurrentValue()); // Раненый персонаж сохранил свое повреждение

        // 5. Проверка, что после десериализации формулы продолжают работать
        restoredChar.changeStatByName("тело", 1); // тело = 13, max хп = 26, cur хп = 19 + 2 = 21 (стратегия UP)
        assertEquals(26, restoredHp.getMaxValue());
        assertEquals(21, restoredHp.getCurrentValue());
    }

    @Test
    @DisplayName("Characteer: ошибка InvalidRuleSetException при попытке загрузить персонажа с чужим rulesetId")
    void testCharacteerWithMismatchedRulesetThrowsException() {
        RuleSet ruleSetA = new RuleSetCreator("rule_A").create();
        RuleSet ruleSetB = new RuleSetCreator("rule_B").create();

        Characteer characterA = new CharacteerCreator(ruleSetA, "Hero", "").create();

        CharacteerDto dto = CharacteerMapper.toDto(characterA);
        String json = gson.toJson(dto);

        CharacteerDto importedDto = gson.fromJson(json, CharacteerDto.class);

        assertThrows(InvalidRuleSetException.class, () -> {
            CharacteerMapper.toObj(ruleSetB, importedDto);
        });
    }
}