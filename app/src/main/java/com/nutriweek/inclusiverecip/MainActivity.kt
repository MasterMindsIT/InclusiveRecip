// MainActivity.kt
package com.nutriweek.inclusiverecip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nutriweek.inclusiverecip.core.theme.AccessRecetasTheme

// (Opcional) si usas el seed rápido:
import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.data.model.*
import com.nutriweek.inclusiverecip.ui.AppNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    AccessRecetasTheme {
        // Seed opcional: crea una receta y un plan si no existen
        LaunchedEffect(Unit) {
            if (InMemoryStore.recipes.isEmpty()) {
                val r1 = Recipe(
                    id = "r-ensalada",
                    title = "Ensalada Simple",
                    shortDescription = "Lechuga, tomate y aceite de oliva.",
                    ingredients = listOf(
                        Ingredient("Lechuga", "1 unidad"),
                        Ingredient("Tomate", "2 unidades"),
                        Ingredient("Aceite de oliva", "1 cda")
                    ),
                    steps = listOf(
                        RecipeStep(1, "Lava y corta la lechuga."),
                        RecipeStep(2, "Corta el tomate en cubos."),
                        RecipeStep(3, "Mezcla y añade aceite de oliva.")
                    ),
                    totalTimeMinutes = 10,
                    servings = 2,
                    tags = listOf("rápida", "ligera")
                )
                InMemoryStore.recipes[r1.id] = r1

                InMemoryStore.plans["plan-semanal"] = MealPlan(
                    id = "plan-semanal",
                    name = "Plan semanal base",
                    days = listOf(
                        DayOfWeekPlan(WeekDay.MON, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.TUE, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.WED, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.THU, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.FRI, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.SAT, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.SUN, recipes = listOf("r-ensalada"))
                    )
                )
            }
        }

        Surface { AppNav() }
    }
}

