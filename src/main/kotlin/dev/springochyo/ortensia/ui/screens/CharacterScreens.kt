package dev.springochyo.ortensia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.springochyo.ortensia.ruleset.core.RuleSet
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.CaelestiaButton
import dev.springochyo.ortensia.ui.components.CaelestiaCard
import dev.springochyo.ortensia.ui.components.CaelestiaHeader
import dev.springochyo.ortensia.ui.theme.LocalStrings

// ──────────────────────────────────────────────────────────
// CharacterList
// ──────────────────────────────────────────────────────────

@Composable
fun CharacterListScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    var showNoRulesDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = s.characterListTitle,
            subtitle  = s.characterListSubtitle(vm.characters.size),
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.MainMenu) }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(200.dp),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                // Create tile — UNTOUCHED
                CreateCharacterTile {
                    if (vm.ruleSets.isEmpty()) showNoRulesDialog = true
                    else vm.navigate(Screen.CharacterSelectRuleSet)
                }
            }
            items(vm.characters) { character ->
                CharacterCard(
                    name = character.getName(),
                    ruleSetId = character.getRulesetId()
                ) { vm.navigate(Screen.CharacterSheet(character)) }
            }
        }
    }

    // ── No rules dialog ─────────────────────────────────────
    if (showNoRulesDialog) {
        Dialog(onDismissRequest = { showNoRulesDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(360.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("⚠", fontSize = 40.sp)
                    Text(
                        s.noRulesTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        s.noRulesBody,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    CaelestiaButton(
                        text = s.goToCreateRules,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showNoRulesDialog = false
                            vm.navigate(Screen.RuleSetCreate)
                        }
                    )
                    TextButton(onClick = { showNoRulesDialog = false }) {
                        Text(s.close, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// Create tile — layout UNTOUCHED (as requested)
@Composable
private fun CreateCharacterTile(onClick: () -> Unit) {
    val s = LocalStrings.current
    CaelestiaCard(onClick = onClick) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "+",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    s.createCharacter,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Simplified — only Name + RuleSetId, centered, no fixed height
@Composable
private fun CharacterCard(
    name: String,
    ruleSetId: String,
    onView: () -> Unit
) {
    CaelestiaCard(onClick = onView) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("🧙", fontSize = 28.sp)
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Text(
                    ruleSetId,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────
// CharacterSelectRuleSet
// ──────────────────────────────────────────────────────────

@Composable
fun CharacterSelectRuleSetScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = s.selectRuleSetTitle,
            subtitle  = s.selectRuleSetSubtitle,
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.CharacterList) }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(200.dp),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(vm.ruleSets) { ruleSet ->
                RuleSetSelectCard(ruleSet) {
                    vm.navigate(Screen.CharacterCreate(ruleSet))
                }
            }
        }
    }
}

// Select card also simplified and centered
@Composable
private fun RuleSetSelectCard(ruleSet: RuleSet, onClick: () -> Unit) {
    val s = LocalStrings.current
    CaelestiaCard(onClick = onClick) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 110.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("📜", fontSize = 24.sp)
                Text(
                    ruleSet.getRulesetId(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Text(
                    s.selectAction,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
