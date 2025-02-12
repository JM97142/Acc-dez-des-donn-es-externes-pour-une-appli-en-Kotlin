package com.aura.ui.transfer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityTransferBinding
import kotlinx.coroutines.launch

/**
 * The transfer activity for the app.
 */
class TransferActivity : AppCompatActivity()
{
  /**
   * The binding for the transfer layout.
   */
  private lateinit var binding: ActivityTransferBinding
  private val transferViewModel: TransferViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)
    val transfer = binding.transfer

    setupTextWatchers()
    transferButton()
    observeViewModel()

    transfer.isEnabled = true
  }

  private fun observeViewModel() {
    observeButtonState()
    observeLoadingState()
    observeTransferResult()
    observeUpdatedBalance()
  }

  private fun observeButtonState() {
    lifecycleScope.launch {
      transferViewModel.isButtonEnabled.collect { isEnabled ->
        binding.transfer.isEnabled = isEnabled
      }
    }
  }

  private fun observeLoadingState() {
    lifecycleScope.launch {
      transferViewModel.isLoading.collect { isLoading ->
        binding.loading.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.transfer.isEnabled = if (isLoading) false else true
      }
    }
  }

  private fun observeTransferResult() {
    lifecycleScope.launch {
      transferViewModel.transferResult.collect { isSuccess ->
        if (isSuccess != null) {
          showToast(if (isSuccess) "Transfer successful" else "Transfer failed")
          if (isSuccess) fetchUpdatedBalance()
        }
      }
    }
  }

  private fun observeUpdatedBalance() {
    lifecycleScope.launch {
      transferViewModel.updatedBalance.collect { updatedBalance ->
        updatedBalance?.let {
          sendUpdatedBalanceBack(it)
        }
      }
    }
  }

  private fun setupTextWatchers() {
    val textWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        verifyTransferForm()
      }

      override fun afterTextChanged(s: Editable?) {}
    }

    binding.recipient.addTextChangedListener(textWatcher)
    binding.amount.addTextChangedListener(textWatcher)
  }

  private fun verifyTransferForm() {
    val recipient = binding.recipient.text.toString()
    val amount = binding.amount.text.toString()
    transferViewModel.verifyTransferForm(recipient, amount)
  }

  private fun transferButton() {
    binding.transfer.setOnClickListener {
      val recipient = binding.recipient.text.toString().trim()
      val amount = binding.amount.text.toString().trim()

      if (validateTransferInput(recipient, amount)) {
        transferViewModel.transfer(
          currentUser = intent.extras?.getString("currentUser") ?: "",
          recipient = recipient,
          amount = amount.toDouble()
        )
      }
    }
  }

  private fun validateTransferInput(recipient: String, amount: String): Boolean {
    when {
      recipient.isEmpty() || amount.isEmpty() -> {
        binding.transfer.isEnabled = false
        showToast("Please enter both recipient and amount")
        return false
      }
      amount.toDoubleOrNull() == null || amount.toDouble() <= 0.0 -> {
        showToast("Please enter a valid amount")
        return false
      }
      intent.extras?.getString("currentUser").isNullOrEmpty() -> {
        showToast("User ID is missing")
        return false
      }
    }
    return true
  }

  private suspend fun fetchUpdatedBalance() {
    transferViewModel.updatedBalance(
      intent.extras?.getString("currentUser") ?: ""
    )
  }

  private fun sendUpdatedBalanceBack(updatedBalance: String) {
    val resultIntent = Intent()
    resultIntent.putExtra("updatedBalance", updatedBalance)
    setResult(Activity.RESULT_OK, resultIntent)
    finish() // Finish and go back to the previous activity
  }

  private fun showToast(text: String) {
    Toast.makeText(this@TransferActivity, text, Toast.LENGTH_LONG).show()
  }

}
