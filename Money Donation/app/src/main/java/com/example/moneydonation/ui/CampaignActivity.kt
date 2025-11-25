package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.moneydonation.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class CampaignActivity : ComponentActivity() {
    
    private lateinit var card1: MaterialCardView
    private lateinit var card2: MaterialCardView
    private lateinit var card3: MaterialCardView
    private lateinit var card4: MaterialCardView
    private lateinit var tvCampaign1: TextView
    private lateinit var tvCampaign2: TextView
    private lateinit var tvCampaign3: TextView
    private lateinit var tvCampaign4: TextView
    
    private val campaignTitles = mutableListOf<String>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign)

        // Initialize views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val searchEditText = findViewById<TextInputEditText>(R.id.etSearch)
        
        card1 = findViewById(R.id.cardCampaign1)
        card2 = findViewById(R.id.cardCampaign2)
        card3 = findViewById(R.id.cardCampaign3)
        card4 = findViewById(R.id.cardCampaign4)
        
        tvCampaign1 = findViewById(R.id.tvCampaign1)
        tvCampaign2 = findViewById(R.id.tvCampaign2)
        tvCampaign3 = findViewById(R.id.tvCampaign3)
        tvCampaign4 = findViewById(R.id.tvCampaign4)
        
        // Store campaign titles
        campaignTitles.add(tvCampaign1.text.toString())
        campaignTitles.add(tvCampaign2.text.toString())
        campaignTitles.add(tvCampaign3.text.toString())
        campaignTitles.add(tvCampaign4.text.toString())

        // Set click listeners
        btnBack.setOnClickListener {
            finish()
        }

        val openDonate: (MaterialCardView, TextView) -> Unit = { card, titleView ->
            card.setOnClickListener {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("campaign_title", titleView.text.toString())
                startActivity(intent)
            }
        }
        openDonate(card1, tvCampaign1)
        openDonate(card2, tvCampaign2)
        openDonate(card3, tvCampaign3)
        openDonate(card4, tvCampaign4)


        // Setup search functionality
        setupSearch(searchEditText)
    }
    
    private fun setupSearch(searchEditText: TextInputEditText) {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCampaigns(s?.toString() ?: "")
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun filterCampaigns(query: String) {
        val searchQuery = query.lowercase().trim()
        
        // Filter campaign 1
        val title1 = campaignTitles[0].lowercase()
        if (title1.contains(searchQuery) || searchQuery.isEmpty()) {
            card1.visibility = android.view.View.VISIBLE
        } else {
            card1.visibility = android.view.View.GONE
        }
        
        // Filter campaign 2
        val title2 = campaignTitles[1].lowercase()
        if (title2.contains(searchQuery) || searchQuery.isEmpty()) {
            card2.visibility = android.view.View.VISIBLE
        } else {
            card2.visibility = android.view.View.GONE
        }
        
        // Filter campaign 3
        val title3 = campaignTitles[2].lowercase()
        if (title3.contains(searchQuery) || searchQuery.isEmpty()) {
            card3.visibility = android.view.View.VISIBLE
        } else {
            card3.visibility = android.view.View.GONE
        }
        
        // Filter campaign 4
        val title4 = campaignTitles[3].lowercase()
        if (title4.contains(searchQuery) || searchQuery.isEmpty()) {
            card4.visibility = android.view.View.VISIBLE
        } else {
            card4.visibility = android.view.View.GONE
        }
    }
}
