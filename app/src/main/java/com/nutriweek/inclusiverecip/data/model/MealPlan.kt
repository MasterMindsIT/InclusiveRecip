package com.nutriweek.inclusiverecip.data.model

data class MealPlan(
    val id: String,
    val name: String, // "Plan semanal base"
    val days: List<DayOfWeekPlan>
)