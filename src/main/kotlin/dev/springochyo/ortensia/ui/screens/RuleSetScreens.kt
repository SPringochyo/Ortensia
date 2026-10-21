package dev.springochyo.ortensia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.springochyo.ortensia.ruleset.core.RuleSet
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.CaelestiaCard
import dev.springochyo.ortensia.ui.components.CaelestiaHeader
import dev.springochyo.ortensia.ui.theme.LocalStrings

// ──────────────────────────────────────────────────────────
// RuleSetList
// ──────────────────────────────────────────────────────────

@Composable
fun RuleSetListScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = s.ruleSetListTitle,
            subtitle  = s.ruleSetListSubtitle(vm.ruleSets.size),
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.MainMenu) }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(220.dp),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                CreateRuleSetTile { vm.navigate(Screen.RuleSetCreate) }
            }
            items(vm.ruleSets) { ruleSet ->
                RuleSetCard(ruleSet = ruleSet) { vm.navigate(Screen.RuleSetView(ruleSet)) }
            }
        }
    }
}

@Composable
private fun CreateRuleSetTile(onClick: () -> Unit) {
    val s = LocalStrings.current
    CaelestiaCard(onClick = onClick, modifier = Modifier.height(160.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "+",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    s.createRules,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun RuleSetCard(ruleSet: RuleSet, onView: () -> Unit) {
    val s = LocalStrings.current
    CaelestiaCard(onClick = onView, modifier = Modifier.height(160.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "📜",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    ruleSet.getRulesetId(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1
                )
                Text(
                    s.statsLabel(ruleSet.getStats().size),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    s.ruleSetViewSubtitle + " →",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────
// RuleSetView
// ──────────────────────────────────────────────────────────

@Composable
fun RuleSetViewScreen(ruleSet: RuleSet, vm: AppViewModel) {
    val s = LocalStrings.current
    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = ruleSet.getRulesetId(),
            subtitle  = s.ruleSetViewSubtitle,
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.RuleSetList) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                s.statsLabel(ruleSet.getStats().size),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            if (ruleSet.getStats().isEmpty()) {
                Text(s.noStats, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            ruleSet.getStats().forEach { stat ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stat.getName(),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp
                            )
                            Text(
                                stat.getDescription(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                "${stat.getMinValue()}–${stat.getMaxValue()}",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
