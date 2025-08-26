// core/a11y/AccessibleTextField.kt
package com.nutriweek.inclusiverecip.core.a11y

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AccessibleTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onDone: (() -> Unit)? = null,
    tts: TtsSpeaker? = null,
    onDictateRequest: (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visual = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        visualTransformation = visual,
        modifier = modifier.semantics { contentDescription = label },
        trailingIcon = {
            Row {
                if (tts != null) {
                    IconButton(
                        onClick = { tts.speak("$label: $value") },
                        modifier = Modifier.semantics { contentDescription = "Leer $label" }
                    ) { Icon(Icons.Filled.VolumeUp, contentDescription = null) }
                    Spacer(Modifier.width(4.dp))
                }
                if (onDictateRequest != null) {
                    IconButton(
                        onClick = onDictateRequest,
                        modifier = Modifier.semantics { contentDescription = "Dictar $label" }
                    ) { Icon(Icons.Filled.Mic, contentDescription = null) }
                    Spacer(Modifier.width(4.dp))
                }
                if (isPassword) {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    val cd   = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.semantics { contentDescription = cd }
                    ) { Icon(icon, contentDescription = null) }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone?.invoke() }
        )
    )
}
