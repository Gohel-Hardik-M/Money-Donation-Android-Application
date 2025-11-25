package com.example.moneydonation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.content.edit

class EditProfileActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etName = findViewById<TextInputEditText>(R.id.tvName)
        val etEmail = findViewById<TextInputEditText>(R.id.tvEmail)

        val name = findViewById<TextView>(R.id.name)
        val email = findViewById<TextView>(R.id.email)
//        val etCity = findViewById<TextInputEditText>(R.id.etCity)
//        val etMobile = findViewById<TextInputEditText>(R.id.etMobile)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)  // <-- getting email from Login


        val prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE)

        etName.setText(prefs.getString("name", ""))
        etEmail.setText(prefs.getString("email", ""))
        name.setText(prefs.getString("name", ""))
        email.setText(prefs.getString("email", ""))


//        name.setText(prefs.getString("name", ""))
//        email.setText(prefs.getString("email", ""))

        // Set click listeners
        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {

            val newName = etName.text.toString().trim()
            val userEmail = etEmail.text.toString().trim()

            if (newName.isBlank()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE)
            prefs.edit {

                putString("name", newName)
            }
            updateNameInFirestore(userEmail, newName)
        }
    }
    private fun updateNameInFirestore(email: String, newName: String) {

        db.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snap ->

                if (snap.isEmpty) {
                    Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val userId = snap.documents[0].id

                db.collection("Users")
                    .document(userId)
                    .update("name", newName)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error connecting to server", Toast.LENGTH_SHORT).show()
            }
    }

}
