package com.nutriweek.inclusiverecip
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nutriweek.inclusiverecip.data.DbProvider
import com.nutriweek.inclusiverecip.data.InMemoryStore
import com.nutriweek.inclusiverecip.domain.AuthManager
import com.nutriweek.inclusiverecip.session.SessionPrefs
import com.nutriweek.inclusiverecip.core.util.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class AuthManagerInstrumentedTest {

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext

        // Inicializa igual que tu app
        DbProvider.init(ctx)
        SessionPrefs.init(ctx)

        // Limpia DB y sesión antes de cada test
        DbProvider.db.clearAllTables()
        InMemoryStore.activeUserId = null
        SessionPrefs.setActiveUserId(null)
    }

    @Test
    fun register_creaUsuario_yDejaSesionActiva() {
        val email = "nuevo@ejemplo.com"
        val pass  = "123456"
        val name  = "Usuario Nuevo"

        val result = AuthManager.register(email, pass, name)
        assertTrue(result is Result)

        // Verifica que el usuario exista
        val creado = DbProvider.db.userDao().findByEmail(email)
        assertNotNull(creado)
        assertEquals(name, creado!!.displayName)

        // Sesión activa en memoria y persistida
        assertNotNull(InMemoryStore.activeUserId)
        assertEquals(InMemoryStore.activeUserId, SessionPrefs.getActiveUserId())
    }

    @Test
    fun login_ok_y_falla_contrasena_incorrecta() {
        val email = "demo@ejemplo.com"
        val passOk = "abcdef"

        // Prepara un usuario real
        assertTrue(AuthManager.register(email, passOk, "Demo") is Result.Ok)

        // Logout para probar login "desde cero"
        AuthManager.logout()
        assertNull(InMemoryStore.activeUserId)

        // Login correcto
        val ok = AuthManager.login(email, passOk)
        assertTrue(ok is Result.Ok)
        assertNotNull(InMemoryStore.activeUserId)

        // Login incorrecto
        AuthManager.logout()
        val bad = AuthManager.login(email, "mala123")
        assertTrue(bad is Result.Err)
        assertNull(InMemoryStore.activeUserId)
    }
}