package dev.springochyo.ortensia.ui

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import dev.springochyo.ortensia.characteer.application.CharacteerCreator
import dev.springochyo.ortensia.characteer.application.port.CharacteerRepository
import dev.springochyo.ortensia.characteer.core.Characteer
import dev.springochyo.ortensia.characteer.infrastructure.io.FileCharacteerRepository
import dev.springochyo.ortensia.ruleset.application.RuleSetCreator
import dev.springochyo.ortensia.ruleset.application.port.RuleSetRepository
import dev.springochyo.ortensia.ruleset.core.RuleSet
import dev.springochyo.ortensia.ruleset.infrastructure.io.FileRuleSetRepository

// ──────────────────────────────────────────────────────────
// Navigation hierarchy depth map
// Used to determine animation direction: deeper = slide left, shallower = slide right
// ──────────────────────────────────────────────────────────

sealed interface Screen {
    val depth: Int

    object MainMenu : Screen                                { override val depth = 0 }
    object Settings : Screen                               { override val depth = 1 }
    object RuleSetList : Screen                            { override val depth = 1 }
    object RuleSetCreate : Screen                          { override val depth = 2 }
    data class RuleSetView(val ruleSet: RuleSet) : Screen  { override val depth = 2 }
    object CharacterList : Screen                          { override val depth = 1 }
    object CharacterSelectRuleSet : Screen                 { override val depth = 2 }
    data class CharacterCreate(val ruleSet: RuleSet) : Screen { override val depth = 3 }
    data class CharacterSheet(val character: Characteer) : Screen { override val depth = 2 }
}

// ──────────────────────────────────────────────────────────
// AppViewModel
// ──────────────────────────────────────────────────────────

class AppViewModel {

    private val ruleSetRepo: RuleSetRepository = FileRuleSetRepository()
    private val characterRepo: CharacteerRepository = FileCharacteerRepository()

    var currentScreen = mutableStateOf<Screen>(Screen.MainMenu)
        private set

    // Positive = navigating forward (slide left), negative = going back (slide right)
    var navigationDelta = mutableIntStateOf(0)
        private set

    val ruleSets = mutableStateListOf<RuleSet>()
    val characters = mutableStateListOf<Characteer>()

    init {
        loadFromDisk()
    }

    // ── Navigation ──────────────────────────────────────────

    fun navigate(screen: Screen) {
        navigationDelta.intValue = screen.depth - currentScreen.value.depth
        currentScreen.value = screen
    }

    // ── RuleSet ─────────────────────────────────────────────

    fun createRuleSet(
        id: String,
        stats: List<Triple<String, String, Pair<Short, Short>>>
    ) {
        val creator = RuleSetCreator(id)
        stats.forEach { (name, desc, range) ->
            creator.addStat(name, desc, range.first, range.second)
        }
        val ruleSet = creator.create()
        ruleSetRepo.save(ruleSet)
        ruleSets.add(ruleSet)
        navigate(Screen.RuleSetList)
    }

    // ── Character ───────────────────────────────────────────

    fun createCharacter(
        ruleSet: RuleSet,
        name: String,
        description: String,
        statValues: Map<String, Short>
    ) {
        val creator = CharacteerCreator(ruleSet, name, description)
        val character = creator.create()
        statValues.forEach { (statName, value) ->
            val current = character.getStatByName(statName)
            val diff = (value - current).toShort()
            if (diff != 0.toShort()) {
                character.changeStatByName(statName, diff)
            }
        }
        characterRepo.save(character)
        characters.add(character)
        navigate(Screen.CharacterList)
    }

    fun saveCharacterToDisk(character: Characteer) {
        characterRepo.save(character)
    }

    // ── Disk loading ────────────────────────────────────────

    private fun loadFromDisk() {
        try {
            val ruleSetIds = ruleSetRepo.list()
            ruleSetIds.forEach { id ->
                try {
                    val rs = ruleSetRepo.load(id)
                    ruleSets.add(rs)
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}

        try {
            val charIds = characterRepo.list()
            charIds.forEach { id ->
                try {
                    val parts = id.split("_")
                    val ruleSetId = parts.last()
                    val matchingRuleSet = ruleSets.find { it.getRulesetId() == ruleSetId }
                    if (matchingRuleSet != null) {
                        val char = characterRepo.load(matchingRuleSet, id)
                        characters.add(char)
                    }
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }
}
