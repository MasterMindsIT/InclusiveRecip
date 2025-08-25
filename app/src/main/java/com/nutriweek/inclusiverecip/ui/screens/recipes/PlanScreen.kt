package com.nutriweek.inclusiverecip.ui.screens.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PlanScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Plan semanal")
            Spacer(Modifier.height(8.dp))
            Text("(Aquí mostraremos los días y recetas; accesible y con TTS en el siguiente paso)")
        }
    }
}