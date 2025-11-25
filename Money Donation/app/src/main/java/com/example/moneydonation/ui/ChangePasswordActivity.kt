package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_change_password)
        val email = intent.getStringExtra("email").orEmpty()
		val newPass = findViewById<TextInputEditText>(R.id.etNewPass)
		val confirm = findViewById<TextInputEditText>(R.id.etConfirmPass)
		val reset = findViewById<MaterialButton>(R.id.btnReset)

		reset.setOnClickListener {
			val p = newPass.text?.toString().orEmpty()
			val c = confirm.text?.toString().orEmpty()
			if (p.length >= 6 && p == c) {
                updatePassword(email, p)
			} else {
				Toast.makeText(this, "Password must be 6+ and match", Toast.LENGTH_SHORT).show()
			}
		}
	}

    private fun updatePassword(email: String, newPass: String) {
        db.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snap ->
                if (!snap.isEmpty) {
                    val id = snap.documents[0].id

                    db.collection("Users")
                        .document(id)
                        .update("password", newPass)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Password Updated!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                }
            }
    }
}
