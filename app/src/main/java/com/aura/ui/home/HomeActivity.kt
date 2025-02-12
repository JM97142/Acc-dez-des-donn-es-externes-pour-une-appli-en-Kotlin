package com.aura.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.data.api.ApiService
import com.aura.data.api.NetworkModule
import com.aura.data.model.account.AccountModelResponse
import com.aura.databinding.ActivityHomeBinding
import com.aura.ui.login.LoginActivity
import com.aura.ui.transfer.TransferActivity
import kotlinx.coroutines.launch

/**
 * The home activity for the app.
 */
class HomeActivity : AppCompatActivity()
{

  /**
   * The binding for the home layout.
   */
  private lateinit var binding: ActivityHomeBinding

  /**
   * A callback for the result of starting the TransferActivity.
   */
  private val startTransferActivityForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
      handleTransferResult(result)
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    buttonTransfer()
    handleIntentExtras()
  }

  private fun buttonTransfer() {
    binding.transfer.setOnClickListener {
      startTransferActivityForResult.launch(
        Intent(this@HomeActivity, TransferActivity::class.java).apply {
          putExtra("currentUser", intent.extras?.getString("currentUser"))
        }
      )
    }
  }

  private fun handleIntentExtras() {
    val extras = intent.extras
    val userId = extras?.getString("currentUser") ?: return
    fetchAccountData(userId)
  }

  private fun fetchAccountData(userId: String) {
    lifecycleScope.launch {
      try {
        val accountResponseList = NetworkModule.provideRetrofit().create(ApiService::class.java)
          .getAccounts(userId)

        println(accountResponseList)
        if (accountResponseList.isNotEmpty()) {
          handleAccountResponse(accountResponseList.find {
            it.main
          } ?: accountResponseList[0])
        } else {
          showToast("No account found")
        }
      } catch (e: Exception) {
        handleErrorFetchingData(e)
      }
    }
  }

  // Handle the API response when account data is fetched successfully
  private fun handleAccountResponse(accountResponse: AccountModelResponse) {
    binding.balance.text = "${accountResponse.balance}€"
    showToast("Login successful")
  }

  private fun showToast(message: String) {
    Toast.makeText(this@HomeActivity, message, Toast.LENGTH_LONG).show()
  }

  private fun handleErrorFetchingData(exception: Exception) {
    showToast("Error fetching account data: ${exception.message}")
  }

  private fun handleTransferResult(result: ActivityResult) {
    if (result.resultCode == Activity.RESULT_OK) {
      val updatedBalance = result.data?.getStringExtra("updatedBalance")
      updatedBalance?.let {
        binding.balance.text = it
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.home_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.disconnect -> {
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
