package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
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
    observeAccess()
    formWatchers()

    binding.login.setOnClickListener{
      buttonLogin()
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

  private fun observeAccess() {
    lifecycleScope.launch {
      viewModel.isAccessGranted.collect { id ->
        id?.let {
          accessGranted(it)
        }
      }
    }
  }

  private fun formState() {
    val id = binding.identifier.text.toString()
    val password = binding.password.text.toString()

    viewModel.verifyForm(id, password)
  }

  private fun formWatchers() {
    val textWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        formState()
      }

      override fun afterTextChanged(s: Editable?) {}
    }

    binding.identifier.addTextChangedListener(textWatcher)
    binding.password.addTextChangedListener(textWatcher)
  }

  private fun userLogin(id: String, password: String) {
    lifecycleScope.launch {
      try {
          viewModel.login(id, password)
      }
      catch (e: Exception) {
        Toast.makeText(this@LoginActivity, "Fail to login: ${e.message}", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun buttonLogin() {
    val id = binding.identifier.text.toString()
    val password = binding.password.text.toString()

    userLogin(id, password)
  }

  private fun accessGranted(id: String) {
    val intent = Intent(this, HomeActivity::class.java)
    intent.putExtra("currentUser", id)
    startActivity(intent)

    finish()
  }
}
