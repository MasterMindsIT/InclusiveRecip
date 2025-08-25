package com.nutriweek.inclusiverecip.data.model

/**
 * Campos pensados para lectura por voz y navegación por pasos.
 */
data class Recipe(
    val id: String,
    val title: String,
    val shortDescription: String,
    val ingredients: List<Ingredient>,
    val steps: List<RecipeStep>,
    val totalTimeMinutes: Int,
    val servings: Int,
    val tags: List<String> = emptyList()
)