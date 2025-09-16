package com.nutriweek.inclusiverecip.data

import com.nutriweek.inclusiverecip.data.model.MealPlan
import com.nutriweek.inclusiverecip.data.model.Recipe
import com.nutriweek.inclusiverecip.data.model.User
import java.util.concurrent.ConcurrentHashMap

/**
 * Repositorio en memoria. Simula DB y sesión activa, Object es para que sea Singleton
 * de esa forma persisten los datos y no se crea una nueva instancia.
 */
object InMemoryStore {
    // Simples mapas en memoria
    val users: MutableMap<String, User> = ConcurrentHashMap()
    val recipes: MutableMap<String, Recipe> = ConcurrentHashMap()
    val plans: MutableMap<String, MealPlan> = ConcurrentHashMap()

    // Estado de sesión (solo email de usuario logueado)
    var activeUserId: String? = null
}