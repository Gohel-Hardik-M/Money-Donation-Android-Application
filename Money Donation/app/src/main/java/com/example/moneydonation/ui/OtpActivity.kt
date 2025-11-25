package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

class OtpActivity : ComponentActivity() {
    private lateinit var email: String
    private lateinit var et1: EditText
	private lateinit var et2: EditText
	private lateinit var et3: EditText
	private lateinit var et4: EditText
	private lateinit var et5: EditText
	private lateinit var et6: EditText
	private lateinit var btnVerify: MaterialButton
	private lateinit var tvTimer: TextView
	private lateinit var tvResend: TextView
	private var countDownTimer: CountDownTimer? = null
	private val timerDuration: Long = 60000 // 60 seconds in milliseconds
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_otp)

		initializeViews()
        email = intent.getStringExtra("email").orEmpty()

        setupOtpFields()
		setupTimer()
		setupClickListeners()
		
		// Auto focus first field
		et1.requestFocus()
		showKeyboard(et1)
	}
	
	private fun initializeViews() {
		et1 = findViewById(R.id.otp1)
		et2 = findViewById(R.id.otp2)
		et3 = findViewById(R.id.otp3)
		et4 = findViewById(R.id.otp4)
		et5 = findViewById(R.id.otp5)
		et6 = findViewById(R.id.otp6)
		btnVerify = findViewById(R.id.btnVerify)
		tvTimer = findViewById(R.id.tvTimer)
		tvResend = findViewById(R.id.tvResend)
	}
	
	private fun setupOtpFields() {
		val otpFields = listOf(et1, et2, et3, et4, et5, et6)
		
		otpFields.forEachIndexed { index, editText ->
			editText.addTextChangedListener(object : TextWatcher {
				override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
				
				override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
					if (s?.isNotEmpty() == true && index < otpFields.size - 1) {
						otpFields[index + 1].requestFocus()
					}
					checkOtpComplete()
				}
				
				override fun afterTextChanged(s: Editable?) {}
			})
			
			editText.setOnKeyListener { _, keyCode, event ->
				if (keyCode == KeyEvent.KEYCODE_DEL && 
					event.action == KeyEvent.ACTION_DOWN && 
					editText.text.isEmpty() && 
					index > 0) {
					otpFields[index - 1].requestFocus()
					otpFields[index - 1].text?.clear()
					true
				} else {
					false
				}
			}
		}
	}
	
	private fun checkOtpComplete() {
		val code = listOf(et1, et2, et3, et4, et5, et6)
			.joinToString(separator = "") { it.text?.toString().orEmpty() }
		
		if (code.length == 6) {
			btnVerify.isEnabled = true
			hideKeyboard()
		} else {
			btnVerify.isEnabled = true // Keep enabled, but will validate on click
		}
	}
	
	private fun setupTimer() {
		countDownTimer = object : CountDownTimer(timerDuration, 1000) {
			override fun onTick(millisUntilFinished: Long) {
				val seconds = millisUntilFinished / 1000
				val minutes = seconds / 60
				val secs = seconds % 60
				tvTimer.text = String.format("%02d:%02d", minutes, secs)
			}
			
			override fun onFinish() {
				tvTimer.text = "00:00"
				tvResend.isEnabled = true
				tvResend.alpha = 1.0f
				tvResend.setTextColor(ContextCompat.getColor(this@OtpActivity, R.color.primary_green))
			}
		}
		countDownTimer?.start()
		
		// Initially disable resend
		tvResend.isEnabled = false
		tvResend.alpha = 0.5f
	}
	
	private fun setupClickListeners() {
        btnVerify.setOnClickListener {
            val code = listOf(et1, et2, et3, et4, et5, et6)
                .joinToString(separator = "") { it.text?.toString().orEmpty() }

            if (code.length != 6) {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔥 Step 1: Fetch OTP from Firestore
            db.collection("Otps")
                .document(email)   // Document ID = email (recommended)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {

                        val storedOtp = document.getString("otp")

                        // 🔥 Step 2: Compare user OTP with DB OTP
                        if (storedOtp == code) {
                            Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show()

                            startActivity(
                                Intent(this, ChangePasswordActivity::class.java)
                                    .putExtra("email", email)
                            )

                            finish()

                        } else {
                            Toast.makeText(this, "Invalid OTP. Please try again", Toast.LENGTH_SHORT).show()
                            clearOtpFields()
                        }

                    } else {
                        Toast.makeText(this, "OTP not found. Please resend.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


        tvResend.setOnClickListener {
            if (tvResend.isEnabled) {

                val newOtp = (100000..999999).random().toString()

                val data = hashMapOf(
                    "email" to email,
                    "otp" to newOtp,
                    "timestamp" to System.currentTimeMillis()
                )

                db.collection("Otps")
                    .document(email)
                    .set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "New OTP sent!", Toast.LENGTH_SHORT).show()

                        // Call your EmailService to send email
                        EmailService.sendEmail(
                            to = email,
                            subject = "Your New OTP",
                            message = "Your new OTP is: $newOtp"
                        ) { /* ignore callback */ }

                        clearOtpFields()
                        resetTimer()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to resend OTP", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        val tvBackLogin = findViewById<TextView>(R.id.tvBackLogin)
		tvBackLogin?.setOnClickListener {
			startActivity(Intent(this, LoginActivity::class.java))
			finish()
		}
	}
	
	private fun clearOtpFields() {
		et1.text?.clear()
		et2.text?.clear()
		et3.text?.clear()
		et4.text?.clear()
		et5.text?.clear()
		et6.text?.clear()
		et1.requestFocus()
		showKeyboard(et1)
	}
	
	private fun resetTimer() {
		countDownTimer?.cancel()
		tvResend.isEnabled = false
		tvResend.alpha = 0.5f
		setupTimer()
	}
	
	private fun showKeyboard(editText: EditText) {
		val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
	}
	
	private fun hideKeyboard() {
		val view = currentFocus
		if (view != null) {
			val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
			imm.hideSoftInputFromWindow(view.windowToken, 0)
		}
	}
	
	override fun onDestroy() {
		super.onDestroy()
		countDownTimer?.cancel()
	}
}
