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
import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.data.model.WeekDay
import com.nutriweek.inclusiverecip.domain.MealPlanner

@Composable
fun PlanScreen(
    onRecipeClick: (String) -> Unit = {},
    onOpenAllRecipes: () -> Unit = {}   // 👈 parámetro nuevo
) {
    val plan = MealPlanner.getActivePlan()
    val recipesMap = InMemoryStore.recipes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { this[SemanticsProperties.TestTag] = "plan_screen" },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Plan semanal",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.semantics {
                heading()
                contentDescription = "Título: Plan semanal"
            }
        )

        Spacer(Modifier.height(12.dp))

        // Botón para ir a la lista completa de recetas
        LargeButton(
            text = "Ver todas las recetas",
            onClick = onOpenAllRecipes,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Abrir lista de todas las recetas" }
        )

        Spacer(Modifier.height(16.dp))

        if (plan == null) {
            Text(
                "No hay un plan aún. Crea o carga recetas para ver aquí.",
                style = MaterialTheme.typography.bodyLarge
            )
            return@Column
        }

        val orderedDays = listOf(
            WeekDay.MON, WeekDay.TUE, WeekDay.WED, WeekDay.THU,
            WeekDay.FRI, WeekDay.SAT, WeekDay.SUN
        )

        orderedDays.forEach { day ->
            val dayPlan = plan.days.firstOrNull { it.day == day }
            Spacer(Modifier.height(16.dp))

            Text(
                text = dayLabel(day),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics {
                    heading()
                    contentDescription = "Día: ${dayLabel(day)}"
                }
            )
            Spacer(Modifier.height(8.dp))

            val recipeIds = dayPlan?.recipes.orEmpty()
            if (recipeIds.isEmpty()) {
                Text(
                    "Sin recetas asignadas",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.semantics {
                        contentDescription = "Sin recetas asignadas para ${dayLabel(day)}"
                    }
                )
            } else {
                recipeIds.forEachIndexed { idx, recipeId ->
                    val recipe = recipesMap[recipeId]
                    val title = recipe?.title ?: "Receta $recipeId"
                    LargeButton(
                        text = "${idx + 1}. $title",
                        onClick = { onRecipeClick(recipeId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Abrir receta: $title" }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

private fun dayLabel(d: WeekDay): String = when (d) {
    WeekDay.MON -> "Lunes"
    WeekDay.TUE -> "Martes"
    WeekDay.WED -> "Miércoles"
    WeekDay.THU -> "Jueves"
    WeekDay.FRI -> "Viernes"
    WeekDay.SAT -> "Sábado"
    WeekDay.SUN -> "Domingo"
}
