package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class WalletDetailsActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_details)

        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etWalletType = findViewById<TextInputEditText>(R.id.etWalletType)
        val etWalletPhone = findViewById<TextInputEditText>(R.id.etWalletPhone)
        val etUPIId = findViewById<TextInputEditText>(R.id.etUPIId)
        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue)

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            val walletType = etWalletType.text?.toString() ?: ""
            val walletPhone = etWalletPhone.text?.toString() ?: ""
            val upiId = etUPIId.text?.toString() ?: ""

            // Validation
            if (walletType.isEmpty()) {
                Toast.makeText(this, "Please enter wallet type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (walletPhone.length < 10) {
                Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Return to PaymentActivity with wallet details
            val resultIntent = Intent()
            resultIntent.putExtra("payment_method", "Wallet")
            resultIntent.putExtra("wallet_details_saved", true)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}

