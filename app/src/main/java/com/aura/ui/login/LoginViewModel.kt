package com.aura.ui.login

import androidx.lifecycle.ViewModel
import com.aura.data.api.NetworkModule
import com.aura.data.model.login.LoginModelRequest
import com.aura.data.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    /**
     * StateFlow to enable or disable login button
     */
    private val _isFormCorrect = MutableStateFlow(false)
    val isFormCorrect: StateFlow<Boolean> get() = _isFormCorrect

    /**
     * StateFlow manage loading state
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    //StateFlow to notify if login is successful
    private val _isAccessGranted = MutableStateFlow<String?>(null)
    val isAccessGranted: StateFlow<String?> get() = _isAccessGranted

    /**
     * Verify form state
     */
    fun verifyForm(identifier: String, password: String) {
        _isFormCorrect.value = identifier.isNotEmpty() && password.isNotEmpty()
    }

    /**
     * API Call
     */
    suspend fun login(identifier: String, password: String) {
        _isLoading.value = true
        try {
            val loginRequest = LoginModelRequest(identifier, password)
            val networkModule = NetworkModule.provideRetrofit().create(ApiService::class.java)
            val result = withContext(Dispatchers.IO) {
                networkModule.login(loginRequest)
            }
            _isLoading.value = false


            // Navigation if successful
            if (result.granted) {
                _isAccessGranted.value = identifier //
            }
            else {
                throw Exception("$result")
            }
        }
        catch (error: Exception) {
            _isLoading.value = false
            println(error)
            throw error
        }
    }
}
