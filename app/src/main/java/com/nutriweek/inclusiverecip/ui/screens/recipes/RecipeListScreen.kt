package com.nutriweek.inclusiverecip.ui.screens.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nutriweek.inclusiverecip.core.a11y.LargeButton
import com.nutriweek.inclusiverecip.domain.MealPlanner

@Composable
fun RecipeListScreen(
    onBack: () -> Unit = {},
    onRecipeClick: (String) -> Unit = {}
) {
    val recipes = MealPlanner.allRecipes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { this[SemanticsProperties.TestTag] = "recipe_list_screen" },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Todas las recetas",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.semantics {
                heading()
                contentDescription = "Título: Todas las recetas"
            }
        )
        Spacer(Modifier.height(12.dp))

        if (recipes.isEmpty()) {
            Text(
                "No hay recetas disponibles.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            recipes.forEachIndexed { idx, recipe ->
                LargeButton(
                    text = "${idx + 1}. ${recipe.title}",
                    onClick = { onRecipeClick(recipe.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Abrir receta: ${recipe.title}" }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        LargeButton(
            text = "Volver",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
