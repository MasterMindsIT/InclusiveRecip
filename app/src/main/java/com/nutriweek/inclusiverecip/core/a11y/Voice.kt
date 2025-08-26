package com.nutriweek.inclusiverecip.core.a11y

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

// --- TTS ---
@Composable
fun rememberTtsSpeaker(
    locales: List<Locale> = listOf(
        Locale("es","CL"), Locale("es","MX"), Locale("es","ES"),
        Locale("es","AR"), Locale("es","US")
    )
): TtsSpeaker {
    val context = LocalContext.current
    val speaker = remember { TtsSpeaker(context, locales) }
    DisposableEffect(Unit) { onDispose { speaker.release() } }
    return speaker
}

class TtsSpeaker(context: android.content.Context, locales: List<Locale>) {
    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context) { status ->
            ready = status == TextToSpeech.SUCCESS
            if (ready) {
                var set = false
                for (loc in locales) {
                    val r = tts?.setLanguage(loc)
                    if (r == TextToSpeech.LANG_AVAILABLE || r == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        set = true; break
                    }
                }
                tts?.setPitch(1.0f)
                tts?.setSpeechRate(0.95f)
            }
        }
    }
    fun speak(text: String) { if (ready && text.isNotBlank()) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts-${System.currentTimeMillis()}") }
    fun stop() { tts?.stop() }
    fun release() { tts?.stop(); tts?.shutdown(); tts = null }
}

// --- Dictado / Reconocimiento de voz ---
@Composable
fun rememberSpeechToTextLauncher(
    onResultText: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val data = res.data
            val list = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            list?.firstOrNull()?.let(onResultText)
        }
    }
    return {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CL")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora…")
        }
        launcher.launch(intent)
    }
}
