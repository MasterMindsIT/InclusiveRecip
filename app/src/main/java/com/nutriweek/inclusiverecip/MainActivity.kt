// MainActivity.kt
package com.nutriweek.inclusiverecip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nutriweek.inclusiverecip.core.theme.AccessRecetasTheme
import com.nutriweek.inclusiverecip.core.theme.Surface
import com.nutriweek.inclusiverecip.data.DbProvider

// (Opcional) si usas el seed rápido:
import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.data.model.*
import com.nutriweek.inclusiverecip.domain.AuthManager
import com.nutriweek.inclusiverecip.session.SessionPrefs
import com.nutriweek.inclusiverecip.ui.AppNav
import com.nutriweek.inclusiverecip.ui.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1) Init DB + prefs (una sola vez)
            DbProvider.init(applicationContext)
            SessionPrefs.init(applicationContext)
            val scrollState = rememberScrollState()

            // 2) Restaura sesión persistida a memoria (si existe)
            if (InMemoryStore.activeUserId == null) {
                InMemoryStore.activeUserId = SessionPrefs.getActiveUserId()
            }


            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()

            // 3) Redirección de arranque (solo 1 vez)
            var didRoute by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                if (!didRoute) {
                    val activeId = InMemoryStore.activeUserId
                    val dao = DbProvider.db.userDao()
                    when {
                        // Sesión activa → ir a Plan
                        !activeId.isNullOrBlank() && dao.findById(activeId) != null -> {
                            navController.navigate(Routes.Plan) {
                                popUpTo(Routes.Login) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        // Sin sesión: si no hay usuarios → Register, si hay → Login
                        dao.count() == 0 -> {
                            navController.navigate(Routes.Register) {
                                popUpTo(Routes.Login) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        else -> {
                            // Dejar en Login (startDestination)
                        }
                    }
                    didRoute = true
                }
            }

            // 4) TopBar común
            val activeIdNow = InMemoryStore.activeUserId
            val displayName = remember(activeIdNow, backStackEntry) {
                activeIdNow?.let { DbProvider.db.userDao().findById(it)?.displayName }
            }
            AccessRecetasTheme {
                Scaffold(
                    topBar = {
                        AppTopBar(
                            isLoggedIn = activeIdNow != null,
                            displayName = displayName,
                            onLogout = {
                                AuthManager.logout()
                                navController.navigate(Routes.Login) {
                                    popUpTo(Routes.Login) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    AppNav(
                        nav = navController,
                        modifier = Modifier.padding(innerPadding)
                    )

                }
                App(scrollState = scrollState)
            }
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
                val r2 = Recipe(
                    id = "s-ensalada",
                    title = "Ensalada Simple nueva",
                    shortDescription = "Lechuga, tomate, aceite de oliva y sal.",
                    ingredients = listOf(
                        Ingredient("Lechuga", "1 unidad"),
                        Ingredient("Tomate", "2 unidades"),
                        Ingredient("Aceite de oliva", "1 cda"),
                        Ingredient("Sal Marina", "A gusto")
                    ),
                    steps = listOf(
                        RecipeStep(1, "Lava y corta la lechuga."),
                        RecipeStep(2, "Corta el tomate en cubos."),
                        RecipeStep(3, "Mezcla y añade aceite de oliva."),
                        RecipeStep(4, "Mezcla y añade aceite de oliva.")

                    ),
                    totalTimeMinutes = 10,
                    servings = 2,
                    tags = listOf("rápida", "ligera")
                )
                val r3 = Recipe(
                    id = "t-ensalada",
                    title = "Asado Chileno",
                    shortDescription = "Diferentes cortes de carne y sal.",
                    ingredients = listOf(
                        Ingredient("Carne de lomo", "1 kilo"),
                        Ingredient("Carne de azotillo", "2 kilo"),
                        Ingredient("Longaniza ahumada", "1 tira"),
                        Ingredient("Sal Marina", "A gusto")
                    ),
                    steps = listOf(
                        RecipeStep(1, "Prepara las brasas."),
                        RecipeStep(2, "Pon las carnes."),
                        RecipeStep(3, "Agrega las longanizas."),
                        RecipeStep(4, "Sal al gusto.")

                    ),
                    totalTimeMinutes = 50,
                    servings = 5,
                    tags = listOf("Normal", "Contundente")
                )
                InMemoryStore.recipes[r1.id] = r1
                InMemoryStore.recipes[r2.id] = r2
                InMemoryStore.recipes[r3.id] = r3

                InMemoryStore.plans["plan-semanal"] = MealPlan(
                    id = "plan-semanal",
                    name = "Plan semanal base",
                    days = listOf(
                        DayOfWeekPlan(WeekDay.MON, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.TUE, recipes = listOf("s-ensalada")),
                        DayOfWeekPlan(WeekDay.WED, recipes = listOf("t-ensalada")),
                        DayOfWeekPlan(WeekDay.THU, recipes = listOf("r-ensalada")),
                        DayOfWeekPlan(WeekDay.FRI, recipes = listOf("s-ensalada")),
                        DayOfWeekPlan(WeekDay.SAT, recipes = listOf("t-ensalada")),
                        DayOfWeekPlan(WeekDay.SUN, recipes = listOf("r-ensalada"))
                    )
                )
            }

        }

        //Surface { AppNav() }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppTopBar(
        isLoggedIn: Boolean,
        displayName: String?,
        onLogout: () -> Unit
    ) {
        TopAppBar(
            title = {
                if (isLoggedIn && !displayName.isNullOrBlank()) {
                    Text("Hola, $displayName")
                } else {
                    Text("Bienvenido")
                }
            },
            actions = {
                if (isLoggedIn) {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            }
        )
    }


