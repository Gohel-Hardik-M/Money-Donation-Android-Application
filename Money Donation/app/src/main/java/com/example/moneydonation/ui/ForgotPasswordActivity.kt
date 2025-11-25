package com.example.moneydonation.ui

import EmailService
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPasswordActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val otpCollection = db.collection("Otps")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_forgot_password)

		val email = findViewById<TextInputEditText>(R.id.etEmail)
		val send = findViewById<MaterialButton>(R.id.btnSendOTP)
		val back = findViewById<TextView>(R.id.tvBackToLogin)

		send.setOnClickListener {
			val e = email.text?.toString().orEmpty()

            if (e.isNullOrEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

			if (e.isBlank()) {
				Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
			} else {

                db.collection("Users")
                    .whereEqualTo("email", e)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            // email found → send OTP
                            sendOtpToEmail(e)
                        } else {
                            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }


			}
		}
		back.setOnClickListener { finish() }
	}

    private fun sendOtpToEmail(email: String) {

        val otp = (100000..999999).random().toString()   // 6-digit OTP

        // Save OTP to Firestore
        val data = hashMapOf(
            "email" to email,
            "otp" to otp,
            "timestamp" to System.currentTimeMillis()
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("Otps")
            .document(email) // use email as document ID
            .set(data)
            .addOnSuccessListener {

                // After saving OTP → send email
                EmailService.sendEmail(
                    to = email,
                    subject = "Your OTP Code",
                    message = "Your OTP for password reset is: $otp"
                ) { success ->

                    runOnUiThread {
                        if (success) {
                            Toast.makeText(this, "OTP sent to email", Toast.LENGTH_LONG).show()
                            moveToOtpScreen(email)
                        } else {
                            Toast.makeText(this, "Failed to send OTP email", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save OTP", Toast.LENGTH_SHORT).show()
            }
    }

    private fun moveToOtpScreen(email: String) {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

}
