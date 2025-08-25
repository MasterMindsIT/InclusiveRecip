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

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nutriweek.inclusiverecip.core.a11y.LargeButton



@Composable
fun RegisterScreen(
    vm: RegisterViewModel = viewModel(),
    onBackToLogin: () -> Unit,
    onSuccess: () -> Unit,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focus = LocalFocusManager.current
    val kb = LocalSoftwareKeyboardController.current


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


        OutlinedTextField(
            value = state.displayName,
            onValueChange = vm::onDisplayNameChange,
            label = { Text("Nombre a mostrar") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Nombre a mostrar" },

        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Correo electrónico" },

        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = vm::onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Contraseña" },

        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.confirm,
            onValueChange = vm::onConfirmChange,
            label = { Text("Repetir contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Repetir contraseña" },
         
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