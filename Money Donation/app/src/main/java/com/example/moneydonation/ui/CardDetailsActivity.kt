package com.example.moneydonation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONArray
import org.json.JSONObject

class CardDetailsActivity : ComponentActivity(){
    private lateinit var etCardNumber: TextInputEditText
    private lateinit var etCardHolderName: TextInputEditText
    private lateinit var etExpiryDate: TextInputEditText
    private lateinit var etCVV: TextInputEditText
    private lateinit var btnContinue: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        etCardNumber = findViewById(R.id.etCardNumber)
        etCardHolderName = findViewById(R.id.etCardHolderName)
        etExpiryDate = findViewById(R.id.etExpiryDate)
        etCVV = findViewById(R.id.etCVV)
        btnContinue = findViewById(R.id.btnContinue)

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        // Format card number (add spaces every 4 digits)
        etCardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().replace(" ", "")
                if (text.isNotEmpty() && text.length % 4 == 0 && before < count) {
                    val formatted = text.chunked(4).joinToString(" ")
                    if (formatted != s.toString()) {
                        etCardNumber.setText(formatted)
                        etCardNumber.setSelection(formatted.length)
                    }
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })

        // Format expiry date (MM/YY)
        etExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().replace("/", "")
                if (text.length == 2 && before < count) {
                    etExpiryDate.setText("$text/")
                    etExpiryDate.setSelection(3)
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })

        btnContinue.setOnClickListener {
//            startRazorpayPayment()
            // Return to PaymentActivity with card details
//            val resultIntent = Intent()
//            resultIntent.putExtra("payment_method", "Card")
//            resultIntent.putExtra("card_details_saved", true)
//            setResult(RESULT_OK, resultIntent)
//            finish()
        }
    }
}

