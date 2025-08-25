package com.nutriweek.inclusiverecip.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriweek.inclusiverecip.core.util.Result
import com.nutriweek.inclusiverecip.domain.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
data class RecoverState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false
)
class RecoverViewModel : ViewModel() {
    private val _state = MutableStateFlow(RecoverState())
    val state: StateFlow<RecoverState> = _state


    fun onEmailChange(v: String) { _state.value = _state.value.copy(email = v, error = null) }


    fun onRecover() {
        val s = _state.value
        if (s.email.isBlank()) {
            _state.value = s.copy(error = "Ingresa tu correo.")
            return
        }
        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, error = null)
            when (val res = AuthManager.recover(s.email.trim())) {
                is Result.Ok -> _state.value = _state.value.copy(done = true)
                is Result.Err -> _state.value = _state.value.copy(error = res.message)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}