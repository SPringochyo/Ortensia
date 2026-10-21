package dev.springochyo.ortensia.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.springochyo.ortensia.ui.screens.*
import dev.springochyo.ortensia.ui.theme.*

fun main() = application {
    val windowState = rememberWindowState(width = 1100.dp, height = 720.dp)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ortensia — Менеджер ролевых игр",
        state = windowState
    ) {
        // ── 1. Resolve strings from language setting ─────────────────
        val strings by remember {
            derivedStateOf {
                when (AppSettings.language) {
                    AppLanguage.RU -> StringsRU
                    AppLanguage.EN -> StringsEN
                }
            }
        }

        // ── 2. Scale UI via LocalDensity (correct Measure/Layout) ────
        val baseDensity = LocalDensity.current
        val scaledDensity by remember {
            derivedStateOf {
                Density(
                    density   = baseDensity.density   * AppSettings.uiScale,
                    fontScale = baseDensity.fontScale  * AppSettings.uiScale
                )
            }
        }

        CompositionLocalProvider(
            LocalDensity  provides scaledDensity,
            LocalStrings  provides strings
        ) {
            OrtensiaTheme {
                val vm = remember { AppViewModel() }
                val screen by vm.currentScreen
                val delta  by vm.navigationDelta

                // ── 3. Directional slide animation ───────────────────
                // delta > 0 = going deeper → new screen enters from right
                // delta ≤ 0 = going back   → new screen enters from left
                val slideIn: AnimatedContentTransitionScope<Screen>.() -> EnterTransition = {
                    if (delta >= 0)
                        fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 6 }
                    else
                        fadeIn(tween(220)) + slideInHorizontally(tween(220)) { -it / 6 }
                }
                val slideOut: AnimatedContentTransitionScope<Screen>.() -> ExitTransition = {
                    if (delta >= 0)
                        fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { -it / 6 }
                    else
                        fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { it / 6 }
                }

                // MaterialTheme is available here (inside OrtensiaTheme)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    AnimatedContent(
                        targetState = screen,
                        modifier = Modifier.fillMaxSize(),
                        transitionSpec = { slideIn().togetherWith(slideOut()) },
                        label = "screen_transition"
                    ) { cur ->
                        when (cur) {
                            is Screen.MainMenu            -> MainMenuScreen(vm)
                            is Screen.Settings            -> SettingsScreen(vm)
                            is Screen.RuleSetList         -> RuleSetListScreen(vm)
                            is Screen.RuleSetCreate       -> RuleSetCreateScreen(vm)
                            is Screen.RuleSetView         -> RuleSetViewScreen(cur.ruleSet, vm)
                            is Screen.CharacterList       -> CharacterListScreen(vm)
                            is Screen.CharacterSelectRuleSet -> CharacterSelectRuleSetScreen(vm)
                            is Screen.CharacterCreate     -> CharacterCreateScreen(cur.ruleSet, vm)
                            is Screen.CharacterSheet      -> CharacterSheetScreen(cur.character, vm)
                        }
                    }
                }
            }
        }
    }
}
