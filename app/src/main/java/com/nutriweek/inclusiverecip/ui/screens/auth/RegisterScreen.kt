package com.nutriweek.inclusiverecip.ui.screens.auth


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nutriweek.inclusiverecip.core.a11y.AccessibleTextField
import com.nutriweek.inclusiverecip.core.a11y.LargeButton
import com.nutriweek.inclusiverecip.core.a11y.rememberSpeechToTextLauncher
import com.nutriweek.inclusiverecip.core.a11y.rememberTtsSpeaker


@Composable
fun RegisterScreen(
    vm: RegisterViewModel = viewModel(),
    onBackToLogin: () -> Unit,
    onSuccess: () -> Unit,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focus = LocalFocusManager.current
    val kb = LocalSoftwareKeyboardController.current

    val tts = rememberTtsSpeaker()
    val startDictateName = rememberSpeechToTextLauncher { vm.onDisplayNameChange(it) }
    val startDictatePass  = rememberSpeechToTextLauncher { vm.onPasswordChange(it) }
    val startDictateEmail = rememberSpeechToTextLauncher { vm.onEmailChange(it) }
    val startDictateConfirm  = rememberSpeechToTextLauncher { vm.onConfirmChange(it) }

    fun submit() { kb?.hide(); focus.clearFocus(); vm.onRegister(onSuccess) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .semantics { this[SemanticsProperties.TestTag] = "register_screen" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))


        AccessibleTextField(
            label = "Nombre",
            value = state.displayName,
            onValueChange = vm::onDisplayNameChange,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            tts = tts,
            onDictateRequest = startDictateName,
            supportingText = null,
            isError = state.error?.contains("nombre", ignoreCase = true) == true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        AccessibleTextField(
            label = "Correo electrónico",
            value = state.email,
            onValueChange = vm::onEmailChange,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            tts = tts,
            onDictateRequest = startDictateEmail,
            supportingText = null,
            isError = state.error?.contains("correo", ignoreCase = true) == true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        AccessibleTextField(
            label = "Contraseña",
            value = state.password,
            onValueChange = vm::onPasswordChange,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            tts = tts,
            onDictateRequest = startDictatePass,
            // Señala error si el mensaje habla de contraseña corta o no coincide (por si lo muestras abajo)
            isError = state.error?.contains("contrase", ignoreCase = true) == true,
            supportingText = if (state.error?.contains("6", true) == true) state.error else null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        AccessibleTextField(
            label = "Repetir contraseña",
            value = state.confirm,
            onValueChange = vm::onConfirmChange,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,                       // enviar con el IME
            onDone = { submit() },                            // llama a tu submit de registro
            tts = tts,
            onDictateRequest = startDictateConfirm,
            // Marca error si el mensaje dice que no coinciden
            isError = state.error?.contains("no coinciden", ignoreCase = true) == true,
            supportingText = if (state.error?.contains("no coinciden", true) == true) state.error else null,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.error != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { contentDescription = "Error: ${state.error}" }
            )
        }


        Spacer(Modifier.height(24.dp))
        LargeButton(
            text = if (state.isLoading) "Creando…" else "Crear cuenta",
            onClick = { submit() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )
        Spacer(Modifier.height(12.dp))
        LargeButton(
            text = "Volver al inicio de sesión",
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )
    }
}