package com.nutriweek.inclusiverecip.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.nutriweek.inclusiverecip.ui.screens.auth.LoginScreen
import com.nutriweek.inclusiverecip.ui.screens.auth.RegisterScreen
import com.nutriweek.inclusiverecip.ui.screens.auth.RecoverScreen
import com.nutriweek.inclusiverecip.ui.screens.recipes.PlanScreen
import com.nutriweek.inclusiverecip.ui.screens.recipes.RecipeDetailScreen
import com.nutriweek.inclusiverecip.ui.screens.recipes.RecipeListScreen

@Composable
fun AppNav(
    nav: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = nav,
        startDestination = Routes.Login,
        modifier = modifier
    ) {
        // ---- AUTH ----
        composable(Routes.Login) {
            LoginScreen(
                onGoRegister = { nav.navigate(Routes.Register) },
                onGoRecover  = { nav.navigate(Routes.Recover) },
                onSuccess    = {
                    nav.navigate(Routes.Plan) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Register) {
            RegisterScreen(
                onBackToLogin = { nav.popBackStack() },
                onSuccess = {
                    nav.navigate(Routes.Plan) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Recover) {
            RecoverScreen(onBackToLogin = { nav.popBackStack() })
        }

        // ---- RECIPES ----
        composable(Routes.Plan) {
            PlanScreen(
                onRecipeClick = { id -> nav.navigate(Routes.recipeDetail(id)) },
                onOpenAllRecipes = { nav.navigate(Routes.RecipeList) }
            )
        }
        composable(Routes.RecipeList) {
            RecipeListScreen(
                onBack = { nav.popBackStack() },
                onRecipeClick = { id -> nav.navigate(Routes.recipeDetail(id)) }
            )
        }
        composable(Routes.RecipeDetail) { backStack ->
            val id = backStack.arguments?.getString(Routes.ArgRecipeId) ?: return@composable
            RecipeDetailScreen(
                recipeId = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}