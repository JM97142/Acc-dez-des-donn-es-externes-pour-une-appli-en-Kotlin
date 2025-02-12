package com.aura.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.data.api.ApiService
import com.aura.data.api.NetworkModule
import com.aura.data.model.transfer.TransferModelRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class TransferViewModel : ViewModel() {

    private val _transferResult = MutableStateFlow<Boolean?>(null)
    val transferResult: StateFlow<Boolean?> get() = _transferResult

    private val _updatedBalance = MutableStateFlow<String?>(null)
    val updatedBalance: StateFlow<String?> get() = _updatedBalance

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> get() = _isButtonEnabled

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun verifyTransferForm(recipient: String, amount: String) {
        _isButtonEnabled.value = recipient.isNotEmpty() && amount.isNotEmpty()
    }

    fun transfer(currentUser: String, recipient: String, amount: Double) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val transferRequest = TransferModelRequest(currentUser, recipient, amount)
                val networkModule = NetworkModule.provideRetrofit().create(ApiService::class.java).transfer(transferRequest)

                if (networkModule.result) {
                    _transferResult.value = true

                } else {
                    _transferResult.value = false

                }

            } catch (e: HttpException) {
                _transferResult.value = false
            } catch (e: Exception) {
                _transferResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun updatedBalance(currentUser: String) {
        try {
            val accountResponse = NetworkModule.provideRetrofit().create(ApiService::class.java).getAccounts(currentUser)
            println(accountResponse)
            val updatedBalance = accountResponse.find {
                it.main
            }?.balance ?: accountResponse[0].balance
            _updatedBalance.value = updatedBalance.toString()
        } catch (e: Exception) {
            _updatedBalance.value = "Error updated balance."
        }
    }
}