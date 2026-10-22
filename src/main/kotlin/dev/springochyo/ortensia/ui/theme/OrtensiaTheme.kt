package dev.springochyo.ortensia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

// ══════════════════════════════════════════════════════════════════
// CAELESTIA DESIGN SYSTEM — Color Tokens
// ══════════════════════════════════════════════════════════════════

// ── Neutral base ──────────────────────────────────────────────────
val CaelestiaBg0        = Color(0xFF0B0E14)   // deepest background
val CaelestiaBg1        = Color(0xFF12161F)   // panel / sidebar
val CaelestiaBg2        = Color(0xFF1A202C)   // card surface
val CaelestiaOutline    = Color(0xFF2E384D)   // border default
val CaelestiaOutlineAcc = Color(0x5589DDFF)   // border accent glow
val CaelestiaTextHigh   = Color(0xFFEEFFFF)   // heading / primary text
val CaelestiaTextMid    = Color(0xFFB0BEC5)   // body text

// ── Caelestia Cyan (default) ──────────────────────────────────────
val CyanPrimary         = Color(0xFF89DDFF)
val CyanContainer       = Color(0xFF1A2F3D)
val CyanOnPrimary       = Color(0xFF003548)

// ── Nebula Violet ────────────────────────────────────────────────
val VioletPrimary       = Color(0xFFC792EA)
val VioletContainer     = Color(0xFF2A1F3D)
val VioletOnPrimary     = Color(0xFF1A0938)

// ── Deep Forest / Emerald ────────────────────────────────────────
val EmeraldPrimary      = Color(0xFF80CBC4)
val EmeraldContainer    = Color(0xFF1B2E2B)
val EmeraldOnPrimary    = Color(0xFF00251E)

// ── Celestial Amber (accent across all themes) ──────────────────
val CaelestiaAmber      = Color(0xFFFFCB6B)
val CaelestiaError      = Color(0xFFFF5370)
val CaelestiaSuccess    = Color(0xFFC3E88D)

// ══════════════════════════════════════════════════════════════════
// Theme enum
// ══════════════════════════════════════════════════════════════════

enum class AppTheme(val label_ru: String, val label_en: String) {
    CAELESTIA_CYAN    ("Caelestia Cyan",    "Caelestia Cyan"),
    NEBULA_VIOLET     ("Nebula Violet",     "Nebula Violet"),
    DEEP_EMERALD      ("Deep Emerald",      "Deep Emerald"),
    LIGHT_DAWN        ("Рассвет (светлая)", "Light Dawn"),
    CUSTOM            ("Кастомная",         "Custom"),
}

enum class CustomThemeMode { DARK, LIGHT }

// ══════════════════════════════════════════════════════════════════
// Color Schemes — presets
// ══════════════════════════════════════════════════════════════════

private val SchemeCyan = darkColorScheme(
    primary            = CyanPrimary,
    onPrimary          = CyanOnPrimary,
    primaryContainer   = CyanContainer,
    onPrimaryContainer = CyanPrimary,
    secondary          = CaelestiaAmber,
    onSecondary        = Color(0xFF3B2900),
    tertiary           = CaelestiaSuccess,
    onTertiary         = Color(0xFF1B2800),
    background         = CaelestiaBg0,
    onBackground       = CaelestiaTextHigh,
    surface            = CaelestiaBg2,
    onSurface          = CaelestiaTextHigh,
    surfaceVariant     = CaelestiaBg1,
    onSurfaceVariant   = CaelestiaTextMid,
    outline            = CaelestiaOutline,
    outlineVariant     = CaelestiaOutlineAcc,
    error              = CaelestiaError,
    onError            = CaelestiaTextHigh,
    scrim              = CaelestiaBg0,
)

private val SchemeViolet = darkColorScheme(
    primary            = VioletPrimary,
    onPrimary          = VioletOnPrimary,
    primaryContainer   = VioletContainer,
    onPrimaryContainer = VioletPrimary,
    secondary          = CaelestiaAmber,
    onSecondary        = Color(0xFF3B2900),
    tertiary           = CaelestiaSuccess,
    onTertiary         = Color(0xFF1B2800),
    background         = CaelestiaBg0,
    onBackground       = CaelestiaTextHigh,
    surface            = CaelestiaBg2,
    onSurface          = CaelestiaTextHigh,
    surfaceVariant     = CaelestiaBg1,
    onSurfaceVariant   = CaelestiaTextMid,
    outline            = CaelestiaOutline,
    outlineVariant     = VioletPrimary.copy(alpha = 0.35f),
    error              = CaelestiaError,
    onError            = CaelestiaTextHigh,
    scrim              = CaelestiaBg0,
)

private val SchemeEmerald = darkColorScheme(
    primary            = EmeraldPrimary,
    onPrimary          = EmeraldOnPrimary,
    primaryContainer   = EmeraldContainer,
    onPrimaryContainer = EmeraldPrimary,
    secondary          = CaelestiaAmber,
    onSecondary        = Color(0xFF3B2900),
    tertiary           = CaelestiaSuccess,
    onTertiary         = Color(0xFF1B2800),
    background         = Color(0xFF080E0D),
    onBackground       = CaelestiaTextHigh,
    surface            = Color(0xFF121C1A),
    onSurface          = CaelestiaTextHigh,
    surfaceVariant     = Color(0xFF0F1714),
    onSurfaceVariant   = CaelestiaTextMid,
    outline            = Color(0xFF253530),
    outlineVariant     = EmeraldPrimary.copy(alpha = 0.35f),
    error              = CaelestiaError,
    onError            = CaelestiaTextHigh,
    scrim              = Color(0xFF080E0D),
)

private val SchemeLightDawn = lightColorScheme(
    primary            = Color(0xFF006688),
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFBDE9FF),
    onPrimaryContainer = Color(0xFF001F2A),
    secondary          = Color(0xFFBF8600),
    onSecondary        = Color.White,
    background         = Color(0xFFF4FAFB),
    onBackground       = Color(0xFF0D1C20),
    surface            = Color(0xFFFFFFFF),
    onSurface          = Color(0xFF0D1C20),
    surfaceVariant     = Color(0xFFDCE8EC),
    onSurfaceVariant   = Color(0xFF3F5760),
    outline            = Color(0xFF6F8F9A),
    outlineVariant     = Color(0xFFC0D5DD),
    error              = Color(0xFFB3261E),
    onError            = Color.White,
)

// ══════════════════════════════════════════════════════════════════
// Custom theme builder
// ══════════════════════════════════════════════════════════════════

/**
 * Parses a HEX string like "#1e1e2e" or "1e1e2e" into [Color].
 * Returns null on invalid input.
 */
fun parseHexColor(hex: String): Color? {
    val clean = hex.trim().removePrefix("#")
    if (clean.length != 6) return null
    return try {
        val r = clean.substring(0, 2).toInt(16)
        val g = clean.substring(2, 4).toInt(16)
        val b = clean.substring(4, 6).toInt(16)
        Color(r, g, b)
    } catch (_: NumberFormatException) { null }
}

/**
 * Generates a full M3 color scheme from two colors + a base mode.
 * Derives surface, bg-secondary, border and text tokens automatically.
 */
fun buildCustomScheme(
    primaryBg: Color,
    accent: Color,
    mode: CustomThemeMode
): androidx.compose.material3.ColorScheme {
    return if (mode == CustomThemeMode.DARK) {
        // Derive dark tones: bg0 = primaryBg darkened, bg1 = slightly lighter, surface = lighter still
        val bg0   = primaryBg.darken(0.0f)
        val bg1   = primaryBg.lighten(0.08f)
        val bg2   = primaryBg.lighten(0.15f)
        val outline  = primaryBg.lighten(0.22f)
        val textHigh = Color(0xFFEEFFFF)
        val textMid  = Color(0xFFB0BEC5)
        darkColorScheme(
            primary            = accent,
            onPrimary          = bg0,
            primaryContainer   = bg1,
            onPrimaryContainer = accent,
            secondary          = accent.harmonize(Color(0xFFFFCB6B)),
            onSecondary        = bg0,
            tertiary           = accent.harmonize(Color(0xFFC3E88D)),
            onTertiary         = bg0,
            background         = bg0,
            onBackground       = textHigh,
            surface            = bg2,
            onSurface          = textHigh,
            surfaceVariant     = bg1,
            onSurfaceVariant   = textMid,
            outline            = outline,
            outlineVariant     = accent.copy(alpha = 0.35f),
            error              = CaelestiaError,
            onError            = textHigh,
            scrim              = bg0,
        )
    } else {
        val bg0   = primaryBg.lighten(0.82f).coerceLight()
        val bg1   = primaryBg.lighten(0.65f).coerceLight()
        val bg2   = Color(0xFFFFFFFF)
        val outline = primaryBg.lighten(0.40f).coerceLight()
        val textHigh = primaryBg.darken(0.7f)
        val textMid  = primaryBg.darken(0.4f)
        lightColorScheme(
            primary            = accent.darken(0.15f),
            onPrimary          = Color.White,
            primaryContainer   = accent.lighten(0.55f).coerceLight(),
            onPrimaryContainer = accent.darken(0.3f),
            secondary          = primaryBg,
            onSecondary        = Color.White,
            background         = bg0,
            onBackground       = textHigh,
            surface            = bg2,
            onSurface          = textHigh,
            surfaceVariant     = bg1,
            onSurfaceVariant   = textMid,
            outline            = outline,
            outlineVariant     = outline.copy(alpha = 0.5f),
            error              = Color(0xFFB3261E),
            onError            = Color.White,
        )
    }
}

// ── Color math helpers ────────────────────────────────────────────

private fun Color.darken(fraction: Float): Color {
    val f = 1f - fraction.coerceIn(0f, 1f)
    return Color(red * f, green * f, blue * f, alpha)
}

private fun Color.lighten(fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red   + (1f - red)   * f,
        green + (1f - green) * f,
        blue  + (1f - blue)  * f,
        alpha
    )
}

/** Blend two colors equally for harmonizing accent with secondary palette. */
private fun Color.harmonize(other: Color, weight: Float = 0.3f): Color = Color(
    red   * (1f - weight) + other.red   * weight,
    green * (1f - weight) + other.green * weight,
    blue  * (1f - weight) + other.blue  * weight,
    alpha
)

/** Ensure light-mode colors don't go below a luminance threshold. */
private fun Color.coerceLight(): Color {
    val minBrightness = 0.75f
    val brightness = (red * 0.299f + green * 0.587f + blue * 0.114f)
    return if (brightness < minBrightness) lighten(minBrightness - brightness) else this
}

// ══════════════════════════════════════════════════════════════════
// AppSettings — global reactive state
// ══════════════════════════════════════════════════════════════════

object AppSettings {
    var theme    by mutableStateOf(AppTheme.CAELESTIA_CYAN)
    var uiScale  by mutableStateOf(1.0f)
    var language by mutableStateOf(AppLanguage.RU)

    // Custom theme state
    var customMode       by mutableStateOf(CustomThemeMode.DARK)
    var customPrimaryHex by mutableStateOf("#1e1e2e")
    var customAccentHex  by mutableStateOf("#cba6f7")
}

// ══════════════════════════════════════════════════════════════════
// OrtensiaTheme
// ══════════════════════════════════════════════════════════════════

@Composable
fun OrtensiaTheme(content: @Composable () -> Unit) {
    val colorScheme = when (AppSettings.theme) {
        AppTheme.CAELESTIA_CYAN  -> SchemeCyan
        AppTheme.NEBULA_VIOLET   -> SchemeViolet
        AppTheme.DEEP_EMERALD    -> SchemeEmerald
        AppTheme.LIGHT_DAWN      -> SchemeLightDawn
        AppTheme.CUSTOM          -> {
            val primaryColor = parseHexColor(AppSettings.customPrimaryHex) ?: CaelestiaBg0
            val accentColor  = parseHexColor(AppSettings.customAccentHex)  ?: CyanPrimary
            buildCustomScheme(primaryColor, accentColor, AppSettings.customMode)
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
