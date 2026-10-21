package dev.springochyo.ortensia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.CaelestiaHeader
import dev.springochyo.ortensia.ui.components.SectionLabel
import dev.springochyo.ortensia.ui.theme.*

// Preview colours for each theme swatch (bg + accent)
private val themeSwatches = mapOf(
    AppTheme.CAELESTIA_CYAN  to Pair(Color(0xFF0B0E14), Color(0xFF89DDFF)),
    AppTheme.NEBULA_VIOLET   to Pair(Color(0xFF0B0E14), Color(0xFFC792EA)),
    AppTheme.DEEP_EMERALD    to Pair(Color(0xFF080E0D), Color(0xFF80CBC4)),
    AppTheme.LIGHT_DAWN      to Pair(Color(0xFFF4FAFB), Color(0xFF006688)),
)

private val scaleOptions = listOf(0.8f, 0.9f, 1.0f, 1.1f, 1.25f, 1.5f)

@Composable
fun SettingsScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title    = s.settingsTitle,
            subtitle = s.settingsSubtitle,
            backLabel = s.back,
            onBack   = { vm.navigate(Screen.MainMenu) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            // ── Color theme ──────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionLabel(s.colorThemeSection)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppTheme.entries.forEach { theme ->
                        ThemeRow(theme = theme, selected = AppSettings.theme == theme) {
                            AppSettings.theme = theme
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // ── UI scale ─────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionLabel(s.scaleSection)
                Text(
                    s.currentScale("%.0f".format(AppSettings.uiScale * 100).toInt()),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )

                Slider(
                    value = AppSettings.uiScale,
                    onValueChange = { AppSettings.uiScale = it },
                    valueRange = 0.7f..1.6f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    scaleOptions.forEach { scale ->
                        val isSelected = AppSettings.uiScale == scale
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { AppSettings.uiScale = scale }
                        ) {
                            Text(
                                "${"%.0f".format(scale * 100)}%",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // ── Language ─────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionLabel(s.languageSection)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppLanguage.entries.forEach { lang ->
                        val isSelected = AppSettings.language == lang
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { AppSettings.language = lang }
                                .then(
                                    if (isSelected) Modifier.border(
                                        1.5.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(10.dp)
                                    ) else Modifier
                                )
                        ) {
                            Text(
                                lang.label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeRow(theme: AppTheme, selected: Boolean, onClick: () -> Unit) {
    val swatch = themeSwatches[theme] ?: Pair(Color(0xFF0B0E14), Color(0xFF89DDFF))
    val (bg, accent) = swatch

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .then(
                if (selected) Modifier.border(
                    1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)
                ) else Modifier
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Color swatch preview
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bg)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(accent)
                )
            }

            val s = LocalStrings.current
            val label = when (AppSettings.language) {
                AppLanguage.RU -> theme.label_ru
                AppLanguage.EN -> theme.label_en
            }
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            if (selected) {
                Text(
                    "✓",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
