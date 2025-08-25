package com.nutriweek.inclusiverecip.domain

import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.data.model.MealPlan
import com.nutriweek.inclusiverecip.data.model.Recipe

object MealPlanner {
    fun getActivePlan(): MealPlan? = InMemoryStore.plans.values.firstOrNull()
    fun allRecipes(): List<Recipe> = InMemoryStore.recipes.values.sortedBy { it.title }
}