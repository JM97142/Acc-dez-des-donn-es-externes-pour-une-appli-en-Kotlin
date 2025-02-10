package com.aura.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class LoginViewModel : ViewModel() {

    private val _isFormCorrect = MutableStateFlow(false)
    val isFormCorrect: StateFlow<Boolean> get() = _isFormCorrect

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun verifyForm(id: String, password: String) {
        _isFormCorrect.value = id.isNotEmpty() && password.isNotEmpty()
    }
}
