package dev.springochyo.ortensia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.springochyo.ortensia.ruleset.core.RuleSet
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.*
import dev.springochyo.ortensia.ui.theme.LocalStrings

@Composable
fun CharacterCreateScreen(ruleSet: RuleSet, vm: AppViewModel) {
    val s = LocalStrings.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val statValues = remember {
        mutableStateMapOf<String, Short>().also { map ->
            ruleSet.getStats().forEach { st ->
                map[st.getName()] = st.getMinValue()
            }
        }
    }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = s.newCharTitle,
            subtitle  = s.newCharSystemLabel(ruleSet.getRulesetId()),
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.CharacterSelectRuleSet) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            // ── Basic info ──────────────────────────────────
            item {
                SectionLabel(s.basicInfoSection)
                CaelestiaTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = ""
                    },
                    label = s.charNameLabel
                )
            }
            item {
                CaelestiaTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = s.charDescLabel,
                    singleLine = false,
                    modifier = Modifier.heightIn(min = 80.dp)
                )
            }

            // ── Stats ───────────────────────────────────────
            if (ruleSet.getStats().isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    SectionLabel(s.initialStatsSection)
                }
                items(ruleSet.getStats()) { statType ->
                    val currentValue = statValues[statType.getName()] ?: statType.getMinValue()
                    StatControlRow(
                        statName = statType.getName(),
                        currentValue = currentValue,
                        minValue = statType.getMinValue(),
                        maxValue = statType.getMaxValue(),
                        description = statType.getDescription(),
                        onIncrement = {
                            val next = (currentValue + 1).toShort()
                            statValues[statType.getName()] = statType.clamp(next)
                        },
                        onDecrement = {
                            val next = (currentValue - 1).toShort()
                            statValues[statType.getName()] = statType.clamp(next)
                        }
                    )
                }
            }

            // ── Error ───────────────────────────────────────
            if (errorMessage.isNotEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // ── Actions ─────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CaelestiaOutlinedBtn(
                        text = s.cancel,
                        onClick = { vm.navigate(Screen.CharacterSelectRuleSet) },
                        modifier = Modifier.weight(1f)
                    )
                    CaelestiaButton(
                        text = s.save,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (name.isBlank()) {
                                errorMessage = s.errorNameBlank
                                return@CaelestiaButton
                            }
                            vm.createCharacter(
                                ruleSet = ruleSet,
                                name = name,
                                description = description,
                                statValues = statValues.toMap()
                            )
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ──────────────────────────────────────────────────────────
// CharacterSheet — view & edit
// ──────────────────────────────────────────────────────────

@Composable
fun CharacterSheetScreen(
    character: dev.springochyo.ortensia.characteer.core.Characteer,
    vm: AppViewModel
) {
    val s = LocalStrings.current
    val statValues = remember {
        mutableStateMapOf<String, Short>().also { map ->
            character.getStats().forEach { (name, stat) ->
                map[name] = stat.getCurrentValue()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = character.getName(),
            subtitle  = s.newCharSystemLabel(character.getRulesetId()),
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.CharacterList) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            // ── Description ─────────────────────────────────
            if (character.getDescription().isNotBlank()) {
                item {
                    SectionLabel(s.aboutSection)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            character.getDescription(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // ── Stats ───────────────────────────────────────
            if (character.getStats().isNotEmpty()) {
                item {
                    SectionLabel(s.statsSection2)
                }
                items(character.getStats().entries.toList()) { (statName, stat) ->
                    val currentValue = statValues[statName] ?: stat.getCurrentValue()
                    StatControlRow(
                        statName = statName,
                        currentValue = currentValue,
                        minValue = stat.getType().getMinValue(),
                        maxValue = stat.getType().getMaxValue(),
                        description = stat.getType().getDescription(),
                        onIncrement = {
                            character.changeStatByName(statName, 1)
                            statValues[statName] = character.getStatByName(statName)
                            vm.saveCharacterToDisk(character)
                        },
                        onDecrement = {
                            character.changeStatByName(statName, -1)
                            statValues[statName] = character.getStatByName(statName)
                            vm.saveCharacterToDisk(character)
                        }
                    )
                }
            }

            // ── Metadata ────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            s.metaSection,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${s.metaRules}: ${character.getRulesetId()}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                        Text(
                            "${s.metaTotalStats}: ${character.getStats().size}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
