package com.nutriweek.inclusiverecip.data.model
enum class WeekDay { MON, TUE, WED, THU, FRI, SAT, SUN }

data class DayOfWeekPlan(
    val day: WeekDay,
    val recipes: List<String> // IDs de Recipe
)