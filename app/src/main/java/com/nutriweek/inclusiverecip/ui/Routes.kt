package com.nutriweek.inclusiverecip.ui

// ui/Routes.kt
object Routes {
    // Auth
    const val Login = "login"
    const val Register = "register"
    const val Recover = "recover"

    // Recetas
    const val Plan = "plan"
    const val RecipeList = "recipes"

    // Detalle con argumento
    private const val RecipeDetailBase = "recipe"
    const val ArgRecipeId = "id"
    const val RecipeDetail = "$RecipeDetailBase/{$ArgRecipeId}"

    // Helper para navegar al detalle con ID concreto
    fun recipeDetail(id: String) = "$RecipeDetailBase/$id"
}

