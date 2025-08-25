package com.nutriweek.inclusiverecip.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriweek.inclusiverecip.core.util.Result
import com.nutriweek.inclusiverecip.domain.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class RegisterState(
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)


class RegisterViewModel : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state


    fun onDisplayNameChange(v: String) { _state.value = _state.value.copy(displayName = v, error = null) }
    fun onEmailChange(v: String) { _state.value = _state.value.copy(email = v, error = null) }
    fun onPasswordChange(v: String) { _state.value = _state.value.copy(password = v, error = null) }
    fun onConfirmChange(v: String) { _state.value = _state.value.copy(confirm = v, error = null) }


    fun onRegister(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.displayName.isBlank() || s.email.isBlank() || s.password.isBlank() || s.confirm.isBlank()) {
            _state.value = s.copy(error = "Completa todos los campos.")
            return
        }
        if (s.password.length < 6) {
            _state.value = s.copy(error = "La contraseña debe tener al menos 6 caracteres.")
            return
        }
        if (s.password != s.confirm) {
            _state.value = s.copy(error = "Las contraseñas no coinciden.")
            return
        }
        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, error = null)
            when (val res = AuthManager.register(s.email.trim(), s.password, s.displayName.trim())) {
                is Result.Ok -> onSuccess()
                is Result.Err -> _state.value = _state.value.copy(error = res.message)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}