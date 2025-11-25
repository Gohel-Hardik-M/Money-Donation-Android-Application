package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONArray
import org.json.JSONObject

class PaymentActivity : ComponentActivity(), PaymentResultListener {
    private var selectedAmount = 1000
    private var selectedPaymentMethod = "Card"
    private var cardDetailsSaved = false
    private var walletDetailsSaved = false

    
    private val walletDetailsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            walletDetailsSaved = result.data?.getBooleanExtra("wallet_details_saved", false) ?: false
            if (walletDetailsSaved) {
                Toast.makeText(this, "Wallet details saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        Checkout.preload(applicationContext)
        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnCard = findViewById<MaterialCardView>(R.id.cardCard)
        val btnWallet = findViewById<MaterialCardView>(R.id.cardWallet)
        val btnConfirm = findViewById<MaterialButton>(R.id.btnConfirm)
        val etCustomAmount = findViewById<TextInputEditText>(R.id.etCustomAmount)
        val tvDonationAmount = findViewById<TextView>(R.id.tvDonationAmount)
        val tvPlatformFee = findViewById<TextView>(R.id.tvPlatformFee)

        // Amount selection buttons
        val amountButtons = listOf(
            findViewById<MaterialButton>(R.id.btnAmount1000),
            findViewById<MaterialButton>(R.id.btnAmount2500),
            findViewById<MaterialButton>(R.id.btnAmount5000),
            findViewById<MaterialButton>(R.id.btnAmount10000),
            findViewById<MaterialButton>(R.id.btnAmount20000),
            findViewById<MaterialButton>(R.id.btnAmount50000)
        )

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        // Amount button listeners
        amountButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val amounts = listOf(1000, 2500, 5000, 10000, 20000, 50000)
                selectedAmount = amounts[index]
                updateAmountDisplay()
                updateSummary()
            }
        }

        // Payment method selection
        btnCard.setOnClickListener {
            selectedPaymentMethod = "Card"
            updatePaymentMethodSelection()
            // Navigate to Card Details screen
//            val intent = Intent(this, CardDetailsActivity::class.java)
//                .putExtra("Money", selectedAmount)
//            cardDetailsLauncher.launch(intent)
        }

        btnWallet.setOnClickListener {
            selectedPaymentMethod = "Wallet"
            updatePaymentMethodSelection()
            // Navigate to Wallet Details screen
            val intent = Intent(this, WalletDetailsActivity::class.java)
            walletDetailsLauncher.launch(intent)
        }

        // Custom amount listener
        etCustomAmount.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val customAmount = etCustomAmount.text.toString().replace("₹", "").replace(",", "").toIntOrNull() ?: 0
                if (customAmount > 0) {
                    selectedAmount = customAmount
                    updateAmountDisplay()
                    updateSummary()
                }
            }
        }

        btnConfirm.setOnClickListener {
            startRazorpayPayment()
        }

        // Initialize display
        updateAmountDisplay()
        updatePaymentMethodSelection()
        updateSummary()
    }

    private fun updateAmountDisplay() {
        val etCustomAmount = findViewById<TextInputEditText>(R.id.etCustomAmount)
        etCustomAmount.setText("₹$selectedAmount")
    }

    private fun updatePaymentMethodSelection() {
        val btnCard = findViewById<MaterialCardView>(R.id.cardCard)
        val btnWallet = findViewById<MaterialCardView>(R.id.cardWallet)
        val checkCard = findViewById<ImageView>(R.id.checkCard)
        val checkWallet = findViewById<ImageView>(R.id.checkWallet)
        val iconCard = findViewById<ImageView>(R.id.iconCard)
        val iconWallet = findViewById<ImageView>(R.id.iconWallet)
        
        // Card selection
        if (selectedPaymentMethod == "Card") {
            btnCard.setCardBackgroundColor(resources.getColor(R.color.light_green, null))
            btnCard.strokeColor = resources.getColor(R.color.primary_green, null)
            btnCard.strokeWidth = 3
            checkCard.visibility = android.view.View.VISIBLE
            iconCard.setColorFilter(resources.getColor(R.color.primary_green, null))
        } else {
            btnCard.setCardBackgroundColor(resources.getColor(R.color.card_background, null))
            btnCard.strokeColor = resources.getColor(R.color.divider_color, null)
            btnCard.strokeWidth = 2
            checkCard.visibility = android.view.View.GONE
            iconCard.setColorFilter(resources.getColor(R.color.primary_green, null))
        }
        
        // Wallet selection
        if (selectedPaymentMethod == "Wallet") {
            btnWallet.setCardBackgroundColor(resources.getColor(R.color.light_green, null))
            btnWallet.strokeColor = resources.getColor(R.color.primary_green, null)
            btnWallet.strokeWidth = 3
            checkWallet.visibility = android.view.View.VISIBLE
            iconWallet.setColorFilter(resources.getColor(R.color.primary_green, null))
            iconWallet.setColorFilter(resources.getColor(R.color.primary_green, null))
        } else {
            btnWallet.setCardBackgroundColor(resources.getColor(R.color.card_background, null))
            btnWallet.strokeColor = resources.getColor(R.color.divider_color, null)
            btnWallet.strokeWidth = 2
            checkWallet.visibility = android.view.View.GONE
            iconWallet.setColorFilter(resources.getColor(R.color.primary_green, null))
        }
    }

    private fun updateSummary() {
        val tvDonationAmount = findViewById<TextView>(R.id.tvDonationAmount)
        val tvPlatformFee = findViewById<TextView>(R.id.tvPlatformFee)
        
        tvDonationAmount.text = "Donation ₹$selectedAmount"
        tvPlatformFee.text = "Platform fee ₹0"
    }

    private fun startRazorpayPayment() {
        // Razorpay checkout object
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_D89mOflBbBSKZi")   // TODO: Replace with your Razorpay Test Key


        try {
            val options = JSONObject()
            options.put("name", "Money Donation")
            options.put("description", "Card Payment")
            options.put("currency", "INR")
            options.put("amount",  (selectedAmount * 100))

            // Prefill (ONLY allowed fields)
            val prefill = JSONObject()
            prefill.put("email", "hemalrashiya037@gmail.com")
            prefill.put("contact", "6352359094")
            prefill.put("name", "Hemal Rashiya")  // Allowed
            options.put("prefill", prefill)

            /** Force card page */
            val cardBlock = JSONObject()
            val instruments = JSONObject()
            instruments.put("method", "card")

            val blocks = JSONObject()
            cardBlock.put("name", "Pay Using Card")
            cardBlock.put("instruments", JSONArray().put(instruments))
            blocks.put("card", cardBlock)

            val display = JSONObject()
            display.put("blocks", blocks)
            display.put("sequence", JSONArray().put("card"))

            val preferences = JSONObject()
            preferences.put("show_default_blocks", false)
            display.put("preferences", preferences)

            val config = JSONObject()
            config.put("display", display)

            options.put("config", config)
            checkout.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }

    }

    // Payment success callback
    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Your donation money successfully completed.", Toast.LENGTH_LONG).show()
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)

        val name = sharedPref.getString("name","")
        val email = sharedPref.getString("email","")
        val campaignName = intent.getStringExtra("campaign_title")

        savePaymentToFirestore(
            paymentId = razorpayPaymentId ?: "",
            amount = selectedAmount,  // your amount
            email = email,
            phone = "6352359094",
            name = name,
            method = "Card",
            campaignName = campaignName
        )
    }

    // Payment failure callback
    override fun onPaymentError(code: Int, description: String?) {
        Toast.makeText(this, "Payment Failed: $description", Toast.LENGTH_LONG).show()
    }

    private fun savePaymentToFirestore(
        paymentId: String,
        amount: Int,
        email: String?,
        phone: String,
        name: String?,
        method: String,
        campaignName: String?
    ) {
        val db = FirebaseFirestore.getInstance()

        val data = hashMapOf(
            "paymentId" to paymentId,
            "amount" to amount,
            "email" to email,
            "phone" to phone,
            "name" to name,
            "method" to method,
            "campaignName" to campaignName,
            "status" to "success",
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("Payments")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Payment saved in database!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save payment!", Toast.LENGTH_SHORT).show()
            }
    }

}
