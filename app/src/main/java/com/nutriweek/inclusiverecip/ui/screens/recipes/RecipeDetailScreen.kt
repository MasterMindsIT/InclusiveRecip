package com.nutriweek.inclusiverecip.ui.screens.recipes

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nutriweek.inclusiverecip.core.a11y.LargeButton
import com.nutriweek.inclusiverecip.data.InMemoryStore
import java.util.Locale

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onBack: () -> Unit = {}
) {
    val recipe = InMemoryStore.recipes[recipeId]

    if (recipe == null) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Receta no encontrada.", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            LargeButton(text = "Volver", onClick = onBack, modifier = Modifier.fillMaxWidth())
        }
        return
    }

    var stepIndex by rememberSaveable { mutableStateOf(0) }
    val totalSteps = recipe.steps.size
    val currentStepText = recipe.steps.getOrNull(stepIndex)?.instruction ?: ""

    // --- TTS helper ---
    val tts = rememberTtsSpeaker()
    DisposableEffect(stepIndex) {
        // Si quieres que lea automáticamente al cambiar de paso, descomenta:
        // tts.speak("Paso ${stepIndex + 1} de $totalSteps. $currentStepText")
        onDispose { /* nada */ }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { this[SemanticsProperties.TestTag] = "recipe_detail_screen" },
        horizontalAlignment = Alignment.Start
    ) {
        // Título
        Text(
            recipe.title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(Modifier.height(8.dp))

        // Meta info
        Text(
            "${recipe.servings} porciones • ${recipe.totalTimeMinutes} minutos",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(recipe.shortDescription, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        // Ingredientes
        Text(
            "Ingredientes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(Modifier.height(8.dp))
        recipe.ingredients.forEach { ing ->
            Text("• ${ing.quantity} — ${ing.name}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
        }

        Spacer(Modifier.height(20.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        // Pasos (con acciones personalizadas para lectores de pantalla)
        Text(
            "Pasos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Paso ${stepIndex + 1} de $totalSteps: $currentStepText",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.semantics {
                customActions = listOf(
                    androidx.compose.ui.semantics.CustomAccessibilityAction(
                        label = "Siguiente paso",
                        action = {
                            if (stepIndex < totalSteps - 1) { stepIndex++; true } else false
                        }
                    ),
                    androidx.compose.ui.semantics.CustomAccessibilityAction(
                        label = "Paso anterior",
                        action = {
                            if (stepIndex > 0) { stepIndex--; true } else false
                        }
                    ),
                    androidx.compose.ui.semantics.CustomAccessibilityAction(
                        label = "Leer paso",
                        action = { tts.speak("Paso ${stepIndex + 1} de $totalSteps. $currentStepText"); true }
                    ),
                    androidx.compose.ui.semantics.CustomAccessibilityAction(
                        label = "Detener lectura",
                        action = { tts.stop(); true }
                    )
                )
            }
        )

        Spacer(Modifier.height(16.dp))

        // Controles grandes
        Row(Modifier.fillMaxWidth()) {
            LargeButton(
                text = "Anterior",
                onClick = { if (stepIndex > 0) stepIndex-- },
                modifier = Modifier.weight(1f),
                enabled = stepIndex > 0
            )
            Spacer(Modifier.width(12.dp))
            LargeButton(
                text = if (stepIndex < totalSteps - 1) "Siguiente" else "Finalizar",
                onClick = { if (stepIndex < totalSteps - 1) stepIndex++ else onBack() },
                modifier = Modifier.weight(1f),
                enabled = totalSteps > 0
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth()) {
            LargeButton(
                text = "🔊 Leer paso",
                onClick = { tts.speak("Paso ${stepIndex + 1} de $totalSteps. $currentStepText") },
                modifier = Modifier.weight(1f),
                enabled = currentStepText.isNotBlank()
            )
            Spacer(Modifier.width(12.dp))
            LargeButton(
                text = "⏹️ Detener",
                onClick = { tts.stop() },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))
        LargeButton(
            text = "Volver",
            onClick = {
                tts.stop()
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Pequeño wrapper de TTS con ciclo de vida Compose:
 * - Inicializa una sola vez por Composable
 * - Idioma: español (intenta Chile/Latam/ES, cae a default si no están)
 * - speak(text): encola el texto
 * - stop(): detiene lectura actual
 */
@Composable
private fun rememberTtsSpeaker(): TtsSpeaker {
    val context = LocalContext.current
    val speaker = remember {
        TtsSpeaker(context)
    }
    DisposableEffect(Unit) {
        onDispose { speaker.release() }
    }
    return speaker
}

private class TtsSpeaker(context: android.content.Context) {
    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context) { status ->
            ready = (status == TextToSpeech.SUCCESS)
            if (ready) {
                // Intenta español Chile, luego España/Latino, luego default
                val langs = listOf(
                    Locale("es", "CL"),
                    Locale("es", "MX"),
                    Locale("es", "ES"),
                    Locale("es", "AR"),
                    Locale("es", "US")
                )
                var set = false
                for (loc in langs) {
                    val res = tts?.setLanguage(loc)
                    if (res == TextToSpeech.LANG_AVAILABLE || res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        set = true; break
                    }
                }
                if (!set) {
                    // deja el idioma por defecto del dispositivo
                }
                tts?.setPitch(1.0f)   // tono normal
                tts?.setSpeechRate(0.95f) // un pelín más lento para claridad
            }
        }
    }

    fun speak(text: String) {
        if (!ready || text.isBlank()) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "step-${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
