// MainActivity.kt
package com.nutriweek.inclusiverecip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
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
        setContent {
            val scrollState = rememberScrollState()
            App(scrollState = scrollState)
        }
    }
}

@Composable
fun App(scrollState: ScrollState) {
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
            if (InMemoryStore.users.size < 5) {
                val demos = listOf(
                    Triple("demo1@ejemplo.com", "123456", "Usuario Demo 1"),
                    Triple("demo2@ejemplo.com", "123456", "Usuario Demo 2"),
                    Triple("demo3@ejemplo.com", "123456", "Usuario Demo 3"),
                    Triple("demo4@ejemplo.com", "123456", "Usuario Demo 4"),
                    Triple("demo5@ejemplo.com", "123456", "Usuario Demo 5"),
                )
                demos.forEach { (email, pass, name) ->
                    val exists = InMemoryStore.users.values.any { it.email.equals(email, ignoreCase = true) }
                    if (!exists) {
                        // Usa FQN para evitar imports extra
                        com.nutriweek.inclusiverecip.domain.AuthManager.register(
                            email = email,
                            password = pass,
                            displayName = name
                        )
                    }
                }
                // Asegura que no quede logueado el último registrado por el seed
                com.nutriweek.inclusiverecip.domain.AuthManager.logout()
            }
        }

        Surface { AppNav() }
    }
}

