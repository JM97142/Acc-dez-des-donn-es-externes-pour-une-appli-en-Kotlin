package com.aura

import com.aura.data.api.ApiService
import com.aura.ui.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoginViewModelTest {
  @Mock
  private lateinit var apiService: ApiService
  private lateinit var loginViewModel: LoginViewModel

  @Before
  fun setUp() {
    loginViewModel = LoginViewModel()
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
  fun login_isCorrect() {

  }
}