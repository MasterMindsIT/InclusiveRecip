package com.nutriweek.inclusiverecip.ui.screens.auth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nutriweek.inclusiverecip.core.a11y.LargeButton

@Composable
fun RecoverScreen(
    vm: RecoverViewModel = viewModel(),
    onBackToLogin: () -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .semantics { this[SemanticsProperties.TestTag] = "recover_screen" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Recuperar cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))


        if (!state.done) {
            OutlinedTextField(
                value = state.email,
                onValueChange = vm::onEmailChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Correo electrónico" },

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
                text = if (state.isLoading) "Enviando…" else "Enviar enlace de recuperación",
                onClick = { vm.onRecover() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )
            Spacer(Modifier.height(12.dp))
            LargeButton(
                text = "Volver al inicio de sesión",
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                "Si el correo existe, hemos enviado instrucciones a tu bandeja.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(24.dp))
            LargeButton(
                text = "Volver al inicio de sesión",
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}