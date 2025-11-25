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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class ProfileActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnEditProfile = findViewById<MaterialCardView>(R.id.cardEditProfile)
        val btnPaymentHistory = findViewById<MaterialCardView>(R.id.cardPaymentHistory)
        val btnSettings = findViewById<MaterialCardView>(R.id.cardSettings)
        val btnManageNotification = findViewById<MaterialCardView>(R.id.cardManageNotification)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)
        val userName = findViewById<TextView>(R.id.userName)
        val userEmail = findViewById<TextView>(R.id.userEmail)

        val prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE)

        userName.setText(prefs.getString("name", ""))
        userEmail.setText(prefs.getString("email", ""))

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        // Edit Profile - Navigate to EditProfileActivity
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // Payment History - Navigate to TransparencyActivity (shows transaction history)
        btnPaymentHistory.setOnClickListener {
            startActivity(Intent(this, TransparencyActivity::class.java).putExtra("profilePayment","pp"))
        }

        // Settings - Navigate to SettingsActivity
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Manage Notification - Navigate to NotificationActivity
        btnManageNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        // Save Button
        btnSave.setOnClickListener {
            Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
