package com.nutriweek.inclusiverecip.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nutriweek.inclusiverecip.core.a11y.LargeButton
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.nutriweek.inclusiverecip.core.a11y.AccessibleTextField
import com.nutriweek.inclusiverecip.core.a11y.rememberSpeechToTextLauncher
import com.nutriweek.inclusiverecip.core.a11y.rememberTtsSpeaker

@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onGoRegister: () -> Unit,
    onGoRecover: () -> Unit,
    onSuccess: () -> Unit,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focus = LocalFocusManager.current
    val kb = LocalSoftwareKeyboardController.current


    val tts = rememberTtsSpeaker()
    val startDictateEmail = rememberSpeechToTextLauncher { vm.onEmailChange(it) }
    val startDictatePass  = rememberSpeechToTextLauncher { vm.onPasswordChange(it) }

    fun submit() {
        kb?.hide(); focus.clearFocus(); vm.onLogin(onSuccess)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .semantics { this[SemanticsProperties.TestTag] = "login_screen" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Acceso a Recetas", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))

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
            imeAction = ImeAction.Done,
            onDone = { submit() },
            tts = tts,
            onDictateRequest = startDictatePass,
            supportingText = null,
            isError = state.error?.contains("contraseña", ignoreCase = true) == true,
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

        // Ingresar
        LargeButton(
            text = if (state.isLoading) "Ingresando…" else "Ingresar",
            onClick = { submit() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )

        Spacer(Modifier.height(12.dp))

        // Crear cuenta
        LargeButton(
            text = "Crear cuenta",
            onClick = onGoRegister, // ✅ ahora se usa el callback
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Botón crear cuenta" },
            enabled = !state.isLoading
        )

        Spacer(Modifier.height(12.dp))

        // Recuperar cuenta
        LargeButton(
            text = "Recuperar cuenta",
            onClick = onGoRecover, // ✅ ahora se usa el callback
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Botón recuperar cuenta" },
            enabled = !state.isLoading
        )
    }
}
