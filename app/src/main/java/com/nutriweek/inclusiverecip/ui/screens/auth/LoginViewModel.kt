package com.nutriweek.inclusiverecip.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriweek.inclusiverecip.domain.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.nutriweek.inclusiverecip.core.util.Result


data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)


class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state


    fun onEmailChange(v: String) { _state.value = _state.value.copy(email = v, error = null) }
    fun onPasswordChange(v: String) { _state.value = _state.value.copy(password = v, error = null) }


    fun onLogin(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _state.value = s.copy(error = "Completa correo y contraseña.")
            return
        }
        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, error = null)
            when (val res = AuthManager.login(s.email.trim(), s.password)) {
                is Result.Ok -> onSuccess()
                is Result.Err -> _state.value = _state.value.copy(error = res.message)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}