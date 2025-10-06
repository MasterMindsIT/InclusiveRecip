package com.nutriweek.inclusiverecip.domain


import com.nutriweek.inclusiverecip.data.DbProvider
import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.data.model.User
import com.nutriweek.inclusiverecip.data.toEntity
import com.nutriweek.inclusiverecip.session.SessionPrefs
import com.nutriweek.inclusiverecip.core.util.Result
import java.util.UUID

object AuthManager {

    fun register(email: String, password: String, displayName: String): Result<Unit> {
        val dao = DbProvider.db.userDao()
        if (dao.findByEmail(email) != null) return Result.Err("El correo ya está registrado.")

        val id = UUID.randomUUID().toString()
        val user =
            User(id = id, email = email, displayName = displayName, hashedPassword = hash(password))
        dao.insert(user.toEntity())

        InMemoryStore.activeUserId = id
        SessionPrefs.setActiveUserId(id)   // <-- persistimos sesión

        return Result.Ok(Unit)
    }

    fun login(email: String, password: String): Result<Unit> {
        val dao = DbProvider.db.userDao()
        val user = dao.findByEmail(email) ?: return Result.Err("Usuario no encontrado.")
        return if (user.hashedPassword == hash(password)) {
            InMemoryStore.activeUserId = user.id
            SessionPrefs.setActiveUserId(user.id)  // <-- persistimos sesión
            Result.Ok(Unit)
        } else Result.Err("Contraseña incorrecta.")
    }

    fun recover(email: String): Result<Unit> {
        val exists = DbProvider.db.userDao().findByEmail(email) != null
        return if (exists) Result.Ok(Unit) else Result.Err("No existe una cuenta con ese correo.")
    }

    fun logout() {
        InMemoryStore.activeUserId = null
        SessionPrefs.setActiveUserId(null)  // <-- limpiamos persistencia
    }

    private fun hash(raw: String) = raw.reversed() // demo
}

