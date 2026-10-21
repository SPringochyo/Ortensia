package dev.springochyo.ortensia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.CaelestiaMenuButton
import dev.springochyo.ortensia.ui.theme.LocalStrings
import kotlin.system.exitProcess

@Composable
fun MainMenuScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    val bgCenter = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    val bgEdge   = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(bgCenter, bgEdge),
                    radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.widthIn(max = 380.dp)
        ) {
            // Logo / Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    "⚔",
                    fontSize = 64.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    s.appTitle,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 3.sp
                    )
                )
                Text(
                    s.appSubtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Decorative divider
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(1.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                            )
                        )
                    )
            )

            Spacer(Modifier.height(4.dp))

            // Main buttons — each has border + hover accent via CaelestiaMenuButton
            CaelestiaMenuButton(
                icon = "📜",
                label = s.menuRules,
                subtitle = s.menuRulesSubtitle
            ) { vm.navigate(Screen.RuleSetList) }

            CaelestiaMenuButton(
                icon = "🧙",
                label = s.menuCharacters,
                subtitle = s.menuCharactersSubtitle
            ) { vm.navigate(Screen.CharacterList) }

            CaelestiaMenuButton(
                icon = "⚙",
                label = s.menuSettings,
                subtitle = s.menuSettingsSubtitle
            ) { vm.navigate(Screen.Settings) }

            Spacer(Modifier.height(4.dp))

            // Exit button
            OutlinedButton(
                onClick = { exitProcess(0) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    s.menuExit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
