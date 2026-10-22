package dev.springochyo.ortensia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.*
import dev.springochyo.ortensia.ui.theme.LocalStrings

// ──────────────────────────────────────────────────────────
// Stat entry for the create form
// ──────────────────────────────────────────────────────────

private data class StatEntry(
    val name: String = "",
    val description: String = "",
    val min: String = "0",
    val max: String = "20"
)

@Composable
fun RuleSetCreateScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    var ruleSetId by remember { mutableStateOf("") }
    val statEntries = remember { mutableStateListOf(StatEntry()) }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = s.createRulesTitle,
            subtitle  = s.createRulesSubtitle,
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.RuleSetList) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            // ── ID ──────────────────────────────────────────
            item {
                SectionLabel(s.ruleSetIdLabel)
                CaelestiaTextField(
                    value = ruleSetId,
                    onValueChange = {
                        ruleSetId = it
                        errorMessage = ""
                    },
                    label = s.ruleSetIdPlaceholder
                )
            }

            // ── Stats section header ────────────────────────
            item {
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel(s.statsSection)
                    TextButton(onClick = { statEntries.add(StatEntry()) }) {
                        Text(
                            s.addStat,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // ── Stat entries ────────────────────────────────
            itemsIndexed(statEntries) { index, entry ->
                StatEntryCard(
                    entry = entry,
                    index = index,
                    canDelete = statEntries.size > 1,
                    onUpdate = { updated -> statEntries[index] = updated },
                    onDelete = { statEntries.removeAt(index) }
                )
            }

            // ── Error ──────────────────────────────────────
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

            // ── Actions ────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CaelestiaOutlinedBtn(
                        text = s.cancel,
                        onClick = { vm.navigate(Screen.RuleSetList) },
                        modifier = Modifier.weight(1f)
                    )
                    CaelestiaButton(
                        text = s.save,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (ruleSetId.isBlank()) {
                                errorMessage = s.errorIdBlank
                                return@CaelestiaButton
                            }
                            val anyInvalid = statEntries.any { it.name.isBlank() }
                            if (anyInvalid) {
                                errorMessage = s.errorStatNameBlank
                                return@CaelestiaButton
                            }
                            val stats = statEntries.map { entry ->
                                Triple(
                                    entry.name,
                                    entry.description,
                                    Pair(
                                        entry.min.toShortOrNull() ?: 0,
                                        entry.max.toShortOrNull() ?: 20
                                    )
                                )
                            }
                            vm.createRuleSet(ruleSetId, stats)
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun StatEntryCard(
    entry: StatEntry,
    index: Int,
    canDelete: Boolean,
    onUpdate: (StatEntry) -> Unit,
    onDelete: () -> Unit
) {
    val s = LocalStrings.current
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    s.statEntryLabel(index + 1),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                if (canDelete) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = s.deleteStatDesc,
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            CaelestiaTextField(
                value = entry.name,
                onValueChange = { onUpdate(entry.copy(name = it)) },
                label = s.statName
            )
            Spacer(Modifier.height(8.dp))
            CaelestiaTextField(
                value = entry.description,
                onValueChange = { onUpdate(entry.copy(description = it)) },
                label = s.statDescription
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CaelestiaTextField(
                    value = entry.min,
                    onValueChange = { onUpdate(entry.copy(min = it)) },
                    label = s.statMin,
                    modifier = Modifier.weight(1f)
                )
                CaelestiaTextField(
                    value = entry.max,
                    onValueChange = { onUpdate(entry.copy(max = it)) },
                    label = s.statMax,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
