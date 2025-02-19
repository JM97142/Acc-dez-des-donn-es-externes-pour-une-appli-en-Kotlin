package com.aura

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aura.data.api.ApiService
import com.aura.data.api.NetworkModule
import com.aura.data.model.transfer.TransferModelResponse
import com.aura.ui.transfer.TransferViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class TransferViewModelTest
{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var transferViewModel: TransferViewModel
    private val apiService = mockk<ApiService>()
    private val dispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {

        Dispatchers.setMain(dispatcher)
        mockkObject(NetworkModule)
        every { NetworkModule.provideRetrofit().create(ApiService::class.java) } returns apiService

        transferViewModel = TransferViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun transferForm_isCorrect() {

        transferViewModel.verifyTransferForm("recipient", "100")
        assertTrue(transferViewModel.isButtonEnabled.value)

        transferViewModel.verifyTransferForm("", "100")
        assertFalse(transferViewModel.isButtonEnabled.value)

        transferViewModel.verifyTransferForm("recipient", "")
        assertFalse(transferViewModel.isButtonEnabled.value)
    }

    @Test
    fun transfer_isCorrect() = runTest {

        val transferResponse = mockk<TransferModelResponse>()

        every { transferResponse.result } returns true

        coEvery { apiService.transfer(any()) } returns transferResponse

        val viewModel = TransferViewModel()

        viewModel.transfer("userTest", "2", 100.0)

        advanceUntilIdle()

        assertTrue(transferResponse.result == true)
    }

    @Test
    fun transfer_isDenied() = runTest {

        val transferResponse = mockk<TransferModelResponse>()

        val response = mockk<Response<TransferModelResponse>>()
        every { response.body() } returns transferResponse
        every { response.code() } returns 500
        every { response.isSuccessful } returns false

        coEvery { apiService.transfer(any()) } throws Exception("Transfer failed")

        val viewModel = TransferViewModel()
        viewModel.transfer("user1", "recipient", 100.0)

        advanceUntilIdle()

        assertTrue(viewModel.transferResult.value == false)
    }
}