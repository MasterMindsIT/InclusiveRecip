package com.nutriweek.inclusiverecip.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Helper de uso rápido: aplica scroll vertical con su propio ScrollState.
 * Úsalo en cualquier contenedor que acepte Modifier:
 *
 *   Column(modifier = Modifier.appVerticalScroll()) { ... }
 *
 * Nota: cada invocación crea su propio rememberScrollState(),
 * así que no interfiere con otras pantallas.
 */
fun Modifier.appVerticalScroll(): Modifier = composed {
    this.verticalScroll(rememberScrollState())
}

/**
 * Contenedor listo para pantallas con scroll.
 * Es un Column que ya trae verticalScroll(rememberScrollState()).
 *
 * Ejemplo:
 * ScreenScaffold(
 *   modifier = Modifier.fillMaxSize().padding(24.dp),
 *   horizontalAlignment = Alignment.CenterHorizontally,
 *   verticalArrangement = Arrangement.Center
 * ) {
 *   // Contenido de la pantalla
 * }
 */
@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scroll),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        content = content
    )
}
