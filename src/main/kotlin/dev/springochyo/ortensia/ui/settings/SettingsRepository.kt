package dev.springochyo.ortensia.ui.settings

import com.google.gson.GsonBuilder
import dev.springochyo.ortensia.ui.theme.*
import java.io.File

// ══════════════════════════════════════════════════════════════════
// Settings DTO — the shape that gets serialized to JSON
// ══════════════════════════════════════════════════════════════════

private data class SettingsDto(
    val language:       String  = "ru",
    val uiScale:        Float   = 1.0f,
    val theme:          String  = "CAELESTIA_CYAN",
    val customBaseMode: String  = "DARK",
    val customPrimary:  String  = "#1e1e2e",
    val customAccent:   String  = "#cba6f7"
)

// ══════════════════════════════════════════════════════════════════
// SettingsRepository — singleton that owns the file I/O
// ══════════════════════════════════════════════════════════════════

object SettingsRepository {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val file = File("storage/settings.json")

    // ── Load ────────────────────────────────────────────────────────

    /**
     * Reads settings.json, applies values to [AppSettings].
     * Falls back to defaults on any error; writes defaults back to disk
     * so the file always exists after this call.
     */
    fun load() {
        val dto: SettingsDto = runCatching {
            if (file.exists()) gson.fromJson(file.readText(), SettingsDto::class.java)
            else null
        }.getOrNull() ?: SettingsDto()

        // Apply to reactive state (safe: resolves unknown enum names to defaults)
        AppSettings.language = runCatching {
            AppLanguage.valueOf(dto.language.uppercase())
        }.getOrDefault(AppLanguage.RU)

        AppSettings.uiScale = dto.uiScale.coerceIn(0.7f, 1.6f)

        AppSettings.theme = runCatching {
            AppTheme.valueOf(dto.theme.uppercase())
        }.getOrDefault(AppTheme.CAELESTIA_CYAN)

        AppSettings.customMode = runCatching {
            CustomThemeMode.valueOf(dto.customBaseMode.uppercase())
        }.getOrDefault(CustomThemeMode.DARK)

        AppSettings.customPrimaryHex = dto.customPrimary.takeIf {
            parseHexColor(it) != null
        } ?: "#1e1e2e"

        AppSettings.customAccentHex = dto.customAccent.takeIf {
            parseHexColor(it) != null
        } ?: "#cba6f7"

        // Always write back so file exists with clean values
        save()
    }

    // ── Save ────────────────────────────────────────────────────────

    /**
     * Serializes current [AppSettings] to storage/settings.json.
     * Creates the directory if it doesn't exist.
     * Swallows IO errors silently — settings are non-critical.
     */
    fun save() {
        runCatching {
            file.parentFile?.mkdirs()
            val dto = SettingsDto(
                language       = AppSettings.language.name.lowercase(),
                uiScale        = AppSettings.uiScale,
                theme          = AppSettings.theme.name,
                customBaseMode = AppSettings.customMode.name,
                customPrimary  = AppSettings.customPrimaryHex,
                customAccent   = AppSettings.customAccentHex
            )
            file.writeText(gson.toJson(dto))
        }
    }
}
