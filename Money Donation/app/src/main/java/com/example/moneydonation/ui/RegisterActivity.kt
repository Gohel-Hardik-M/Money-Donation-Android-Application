package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.example.moneydonation.model.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)

		val name = findViewById<TextInputEditText>(R.id.etFullName)
		val email = findViewById<TextInputEditText>(R.id.etEmail)
		val pass = findViewById<TextInputEditText>(R.id.etPassword)
		val confirm = findViewById<TextInputEditText>(R.id.etConfirmPassword)
		val btn = findViewById<MaterialButton>(R.id.btnRegister)
		val goLogin = findViewById<TextView>(R.id.tvLogin)

		btn.setOnClickListener {
			val n = name.text?.toString().orEmpty()
			val e = email.text?.toString().orEmpty()
			val p = pass.text?.toString().orEmpty()
			val c = confirm.text?.toString().orEmpty()
			if (n.isBlank() || e.isBlank() || p.length < 6 || p != c) {
				Toast.makeText(this, "Check inputs (password 6+, must match)", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
            addUserWithAutoId(n,e,p)
//			Toast.makeText(this, "Registered (demo). Verify via OTP.", Toast.LENGTH_SHORT).show()
//			startActivity(Intent(this, OtpActivity::class.java))
		}
		goLogin.setOnClickListener { finish() }
	}


    private fun addUserWithAutoId(name: String, email: String,password: String) {
        // Get input values from your UI

        // Create user object
        val user = User(
            name = name,
            email = email,
            password = password
        )

        // Add to Firestore (auto-generates document ID)
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                // Success - documentReference.id contains the auto-generated ID
                Log.d("FIRESTORE", "Document added with ID: ${documentReference.id}")
                Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Error
                Log.w("FIRESTORE", "Error adding document", e)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
