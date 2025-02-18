package com.aura

import com.aura.data.api.ApiService
import com.aura.data.api.NetworkModule
import com.aura.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import kotlin.math.log

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoginViewModelTest {

  private lateinit var loginViewModel: LoginViewModel
  private val apiService = mockk<ApiService>()
  private val networkModule = mockk<NetworkModule>()

  @Before
  fun setUp() {
    loginViewModel = LoginViewModel()

    mockkObject(networkModule)
    every { networkModule.provideRetrofit().create(ApiService::class.java) } returns apiService
  }

  @Test
  fun form_isCorrect() {
    // Test without password
    loginViewModel.verifyForm("userTest", "")
    assertFalse(loginViewModel.isFormCorrect.value)

    // Test without id
    loginViewModel.verifyForm("", "password")
    assertFalse(loginViewModel.isFormCorrect.value)


    // Test with incorrect id & password
    loginViewModel.verifyForm("userTest", "password")
    assertTrue(loginViewModel.isFormCorrect.value)
  }

  @Test
  fun login_isCorrect() = runTest {

    coEvery { apiService.login(any()) } returns mockk {
      every { granted } returns true
    }

    loginViewModel.login("Test","Test")

    assertEquals("Test", loginViewModel.isAccessGranted.value)
  }

  @Test
  fun login_isDenied() = runTest {

    val response = mockk<Response<Any>>()

    every { response.code() } returns 401
    every { response.message() } returns "Login failure"

    val httpException = HttpException(response)

    coEvery { apiService.login(any()) } throws httpException

    try {
        loginViewModel.login("testLogin", "passwordTest")
    }
    catch (e: Exception) {
      assertTrue(e is HttpException)
    }
  }
}