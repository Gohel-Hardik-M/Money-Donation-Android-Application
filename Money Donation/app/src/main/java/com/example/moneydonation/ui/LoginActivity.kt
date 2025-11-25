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


class LoginActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		val email = findViewById<TextInputEditText>(R.id.etEmail)
		val pass  = findViewById<TextInputEditText>(R.id.etPassword)
		val btn   = findViewById<MaterialButton>(R.id.btnLogin)
		val toReg = findViewById<TextView>(R.id.tvSignUp)
		val forgot = findViewById<TextView>(R.id.tvForgotPassword)

		btn.setOnClickListener {
			val e = email.text?.toString().orEmpty()
			val p = pass.text?.toString().orEmpty()
			if (e.isBlank() || p.length < 6) {
				Toast.makeText(this, "Enter valid email and 6+ char password", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			checkEmailExistsInFirestore(e,p)

		}
		toReg.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
		forgot.setOnClickListener { startActivity(Intent(this, ForgotPasswordActivity::class.java)) }
	}

    private fun checkEmailExistsInFirestore(emailToCheck: String,passwordToCheck: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Users")
            .whereEqualTo("email", emailToCheck.toLowerCase())
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Email doesn't exist
                    Toast.makeText(this, "Incorrect emaill address and password", Toast.LENGTH_SHORT).show()

                } else {
                    // Email exists
                    val document = querySnapshot.documents.first()
                    val existingUser = document.toObject(User::class.java)
                    if (passwordToCheck == existingUser?.password){
                        Toast.makeText(this, "Login Successfully.", Toast.LENGTH_LONG).show()
                        saveUserToPreferences(existingUser)
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show()

                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE", "Error checking email", exception)
            }
    }
    private fun saveUserToPreferences(user: User) {
        val prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString("name", user.name)
        editor.putString("email", user.email)
        editor.apply()
    }

}
