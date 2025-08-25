package com.nutriweek.inclusiverecip.domain

import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.data.model.User
import com.nutriweek.inclusiverecip.core.util.Result
import java.util.UUID

object AuthManager {
    fun register(email: String, password: String, displayName: String): Result<Unit> {
        if (InMemoryStore.users.values.any { it.email.equals(email, true) }) {
            return Result.Err("El correo ya está registrado.")
        }
        val id = UUID.randomUUID().toString()
        val user =
            User(id = id, email = email, displayName = displayName, hashedPassword = hash(password))
        InMemoryStore.users[id] = user
        InMemoryStore.activeUserId = id
        return Result.Ok(Unit)
    }

    fun login(email: String, password: String): Result<Unit> {
        val user = InMemoryStore.users.values.firstOrNull { it.email.equals(email, true) }
            ?: return Result.Err("Usuario no encontrado.")
        return if (user.hashedPassword == hash(password)) {
            InMemoryStore.activeUserId = user.id
            Result.Ok(Unit)
        } else Result.Err("Contraseña incorrecta.")
    }

    fun recover(email: String): Result<Unit> {
        val exists = InMemoryStore.users.values.any { it.email.equals(email, true) }
        return if (exists) Result.Ok(Unit) else Result.Err("No existe una cuenta con ese correo.")
    }

    fun logout() { InMemoryStore.activeUserId = null }

    private fun hash(raw: String) = raw.reversed() // Hash fake solo para demo
}