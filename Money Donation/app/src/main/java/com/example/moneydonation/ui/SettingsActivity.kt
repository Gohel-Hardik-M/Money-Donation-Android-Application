package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val cardProfile = findViewById<MaterialCardView>(R.id.cardProfile)
        val btnSignOut = findViewById<MaterialButton>(R.id.btnSignOut)

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        cardProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnSignOut.setOnClickListener {
            // Handle sign out
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}
