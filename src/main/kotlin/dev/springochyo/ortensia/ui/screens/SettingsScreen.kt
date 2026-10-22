package dev.springochyo.ortensia.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.springochyo.ortensia.ui.AppViewModel
import dev.springochyo.ortensia.ui.Screen
import dev.springochyo.ortensia.ui.components.CaelestiaButton
import dev.springochyo.ortensia.ui.components.CaelestiaHeader
import dev.springochyo.ortensia.ui.components.SectionLabel
import dev.springochyo.ortensia.ui.theme.*

// ── Static swatch preview map ──────────────────────────────────────
private val themeSwatches = mapOf(
    AppTheme.CAELESTIA_CYAN to Pair(Color(0xFF0B0E14), Color(0xFF89DDFF)),
    AppTheme.NEBULA_VIOLET  to Pair(Color(0xFF0B0E14), Color(0xFFC792EA)),
    AppTheme.DEEP_EMERALD   to Pair(Color(0xFF080E0D), Color(0xFF80CBC4)),
    AppTheme.LIGHT_DAWN     to Pair(Color(0xFFF4FAFB), Color(0xFF006688)),
    AppTheme.CUSTOM         to null,   // dynamic — rendered differently
)

private val scaleOptions = listOf(0.8f, 0.9f, 1.0f, 1.1f, 1.25f, 1.5f)

@Composable
fun SettingsScreen(vm: AppViewModel) {
    val s = LocalStrings.current
    Column(modifier = Modifier.fillMaxSize()) {
        CaelestiaHeader(
            title     = s.settingsTitle,
            subtitle  = s.settingsSubtitle,
            backLabel = s.back,
            onBack    = { vm.navigate(Screen.MainMenu) }
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
                        ThemeRow(
                            theme    = theme,
                            selected = AppSettings.theme == theme,
                            onClick  = { AppSettings.theme = theme }
                        )
                    }
                }

                // ── Custom theme editor (shown when CUSTOM is selected) ──
                AnimatedVisibility(
                    visible = AppSettings.theme == AppTheme.CUSTOM,
                    enter   = expandVertically() + fadeIn(),
                    exit    = shrinkVertically() + fadeOut()
                ) {
                    CustomThemeEditor(s)
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
                        thumbColor       = MaterialTheme.colorScheme.primary,
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

// ── Custom Theme Editor ──────────────────────────────────────────

@Composable
private fun CustomThemeEditor(s: AppStrings) {
    // Local edit state — only applied when user taps "Apply" or inputs valid hex
    var primaryHex by remember { mutableStateOf(AppSettings.customPrimaryHex) }
    var accentHex  by remember { mutableStateOf(AppSettings.customAccentHex) }
    var mode       by remember { mutableStateOf(AppSettings.customMode) }
    var primaryErr by remember { mutableStateOf(false) }
    var accentErr  by remember { mutableStateOf(false) }

    fun applyIfValid() {
        val pOk = parseHexColor(primaryHex) != null
        val aOk = parseHexColor(accentHex) != null
        primaryErr = !pOk
        accentErr  = !aOk
        if (pOk && aOk) {
            AppSettings.customPrimaryHex = primaryHex
            AppSettings.customAccentHex  = accentHex
            AppSettings.customMode       = mode
        }
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Base Mode toggle ─────────────────────────────────────
            Text(
                s.customThemeMode,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    CustomThemeMode.DARK  to s.customThemeDark,
                    CustomThemeMode.LIGHT to s.customThemeLight
                ).forEach { (m, label) ->
                    val sel = mode == m
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (sel) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                mode = m
                                AppSettings.customMode = m
                                // live preview — apply immediately
                                val pOk = parseHexColor(primaryHex) != null
                                val aOk = parseHexColor(accentHex) != null
                                if (pOk && aOk) {
                                    AppSettings.customPrimaryHex = primaryHex
                                    AppSettings.customAccentHex  = accentHex
                                }
                            }
                            .then(
                                if (!sel) Modifier.border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(10.dp)
                                ) else Modifier
                            )
                    ) {
                        Text(
                            label,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            color = if (sel) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            // ── Primary hex input ────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    s.customThemePrimary,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Color preview dot
                    val previewPrimary = parseHexColor(primaryHex) ?: MaterialTheme.colorScheme.outline
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(previewPrimary)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )
                    OutlinedTextField(
                        value = primaryHex,
                        onValueChange = { v ->
                            primaryHex = v
                            primaryErr = false
                            // Live preview while typing a valid hex
                            if (parseHexColor(v) != null && parseHexColor(accentHex) != null) {
                                AppSettings.customPrimaryHex = v
                                AppSettings.customAccentHex  = accentHex
                                AppSettings.customMode       = mode
                            }
                        },
                        singleLine = true,
                        isError = primaryErr,
                        placeholder = { Text(s.customThemeHexHint, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                        supportingText = if (primaryErr) {{ Text(s.customThemeInvalidHex, color = MaterialTheme.colorScheme.error, fontSize = 11.sp) }} else null,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            errorBorderColor     = MaterialTheme.colorScheme.error,
                            focusedTextColor     = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor   = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor   = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
            }

            // ── Accent hex input ─────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    s.customThemeAccent,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val previewAccent = parseHexColor(accentHex) ?: MaterialTheme.colorScheme.outline
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(previewAccent)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )
                    OutlinedTextField(
                        value = accentHex,
                        onValueChange = { v ->
                            accentHex = v
                            accentErr = false
                            if (parseHexColor(primaryHex) != null && parseHexColor(v) != null) {
                                AppSettings.customPrimaryHex = primaryHex
                                AppSettings.customAccentHex  = v
                                AppSettings.customMode       = mode
                            }
                        },
                        singleLine = true,
                        isError = accentErr,
                        placeholder = { Text(s.customThemeHexHint, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                        supportingText = if (accentErr) {{ Text(s.customThemeInvalidHex, color = MaterialTheme.colorScheme.error, fontSize = 11.sp) }} else null,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            errorBorderColor     = MaterialTheme.colorScheme.error,
                            focusedTextColor     = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor   = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor   = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
            }

            // ── Apply button ──────────────────────────────────────────
            CaelestiaButton(
                text     = s.customThemeApply,
                modifier = Modifier.fillMaxWidth(),
                onClick  = ::applyIfValid
            )
        }
    }
}

// ── Theme row ──────────────────────────────────────────────────────

@Composable
private fun ThemeRow(theme: AppTheme, selected: Boolean, onClick: () -> Unit) {
    val staticSwatch = themeSwatches[theme]

    // For CUSTOM — show the live custom colors as the swatch preview
    val previewBg: Color = when {
        theme == AppTheme.CUSTOM -> parseHexColor(AppSettings.customPrimaryHex) ?: Color(0xFF0B0E14)
        staticSwatch != null     -> staticSwatch.first
        else                     -> Color(0xFF0B0E14)
    }
    val previewAccent: Color = when {
        theme == AppTheme.CUSTOM -> parseHexColor(AppSettings.customAccentHex) ?: Color(0xFF89DDFF)
        staticSwatch != null     -> staticSwatch.second
        else                     -> Color(0xFF89DDFF)
    }

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
            // Swatch preview
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(previewBg)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(previewAccent)
                )
            }

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
                Text("✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
