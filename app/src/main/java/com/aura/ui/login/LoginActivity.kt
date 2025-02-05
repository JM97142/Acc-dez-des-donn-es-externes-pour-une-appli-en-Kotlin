package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.home.HomeActivity
import kotlinx.coroutines.launch

/**
 * The login activity for the app.
 */
class LoginActivity : AppCompatActivity() {
  /**
   * The binding for the login layout.
   */
  private lateinit var binding: ActivityLoginBinding
  private val viewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    observeForm()
    observeLoading()

    binding.login.setOnClickListener {
      binding.loading.visibility = View.VISIBLE

      val intent = Intent(this@LoginActivity, HomeActivity::class.java)
      startActivity(intent)

      finish()
    }
  }

  private fun observeForm() {
    lifecycleScope.launch {
      viewModel.isFormCorrect.collect { isValid ->
        binding.login.isEnabled = isValid
      }
    }
  }

  private fun observeLoading() {
    lifecycleScope.launch {
      viewModel.isLoading.collect { isLoading ->
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
      }
    }
  }
}
