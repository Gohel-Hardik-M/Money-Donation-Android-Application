package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moneydonation.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : ComponentActivity() {
    
    private lateinit var cardNotification: MaterialCardView
    private lateinit var cardDonation: MaterialCardView
    private lateinit var cardBrowseCampaigns: MaterialCardView
    private lateinit var cardTransparency: MaterialCardView
    
    private val quickToolNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        cardNotification = findViewById(R.id.cardNotification)
        cardDonation = findViewById(R.id.cardDonation)
        cardBrowseCampaigns = findViewById(R.id.cardBrowseCampaigns)
        cardTransparency = findViewById(R.id.cardTransparency)
        val btnProfile = findViewById<ImageView>(R.id.btnProfile)
        val btnSettings = findViewById<ImageView>(R.id.btnSettings)
        val searchEditText = findViewById<TextInputEditText>(R.id.etSearch)

        // Store Quick Tools names for search
        quickToolNames.add("Notification")
        quickToolNames.add("Donation")
        quickToolNames.add("BROWSE CAMPAIGNS")
        quickToolNames.add("TRANSPARENCY & TRUST")

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        cardNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        cardDonation.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

        cardBrowseCampaigns.setOnClickListener {
            startActivity(Intent(this, CampaignActivity::class.java))
        }

        cardTransparency.setOnClickListener {
            startActivity(Intent(this, TransparencyActivity::class.java))
        }

        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Setup search functionality
        setupSearch(searchEditText)
    }
    
    private fun setupSearch(searchEditText: TextInputEditText) {
        // When search bar is clicked, navigate to CampaignActivity
        searchEditText.setOnClickListener {
            val intent = Intent(this, CampaignActivity::class.java)
            // Pass search query if any
            val query = searchEditText.text?.toString() ?: ""
            if (query.isNotEmpty()) {
                intent.putExtra("search_query", query)
            }
            startActivity(intent)
        }
        
        // Real-time search filtering for Quick Tools
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterQuickTools(s?.toString() ?: "")
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun filterQuickTools(query: String) {
        val searchQuery = query.lowercase().trim()
        
        // Filter Notification
        val name1 = quickToolNames[0].lowercase()
        if (name1.contains(searchQuery) || searchQuery.isEmpty()) {
            cardNotification.visibility = android.view.View.VISIBLE
        } else {
            cardNotification.visibility = android.view.View.GONE
        }
        
        // Filter Donation
        val name2 = quickToolNames[1].lowercase()
        if (name2.contains(searchQuery) || searchQuery.isEmpty()) {
            cardDonation.visibility = android.view.View.VISIBLE
        } else {
            cardDonation.visibility = android.view.View.GONE
        }
        
        // Filter Browse Campaigns
        val name3 = quickToolNames[2].lowercase()
        if (name3.contains(searchQuery) || searchQuery.isEmpty()) {
            cardBrowseCampaigns.visibility = android.view.View.VISIBLE
        } else {
            cardBrowseCampaigns.visibility = android.view.View.GONE
        }
        
        // Filter Transparency
        val name4 = quickToolNames[3].lowercase()
        if (name4.contains(searchQuery) || searchQuery.isEmpty()) {
            cardTransparency.visibility = android.view.View.VISIBLE
        } else {
            cardTransparency.visibility = android.view.View.GONE
        }
    }
}
