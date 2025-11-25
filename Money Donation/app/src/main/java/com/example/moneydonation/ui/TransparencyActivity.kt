package com.example.moneydonation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneydonation.R
import com.example.moneydonation.adapter.TransactionAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class TransparencyActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparency)


        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTransactions)
        val totalFund = findViewById<TextView>(R.id.totalFund)
        val fundDistribution = findViewById<TextView>(R.id.fundDistribution)
//
        calculateTotalAmount { total ->
            totalFund.setText("₹$total")
        }

        calculateTotalDistributionAmount { totalFund ->
            fundDistribution.setText("₹$totalFund")
        }
        btnBack.setOnClickListener { finish() }
        val pp = intent.getStringExtra("profilePayment")

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch data from Firestore
        if (pp == "pp"){
fetchSpecificUserTransactions(recyclerView)
        }else{
            fetchTransactions(recyclerView)
        }
    }

    private fun calculateTotalAmount(onResult: (Int) -> Unit) {
        db.collection("Payments")
            .get()
            .addOnSuccessListener { snapshot ->
                var total = 0

                for (doc in snapshot.documents) {
                    val amt = doc.getLong("amount") ?: 0
                    total += amt.toInt()
                }

                onResult(total)
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    private fun calculateTotalDistributionAmount(onResult: (Int) -> Unit) {
        val prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val email = prefs.getString("email","")
        db.collection("Payments")
            .whereEqualTo("email",email)
            .get()
            .addOnSuccessListener { snapshot ->
                var total = 0

                for (doc in snapshot.documents) {
                    val amt = doc.getLong("amount") ?: 0
                    total += amt.toInt()
                }

                onResult(total)
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    private fun fetchTransactions(recyclerView: RecyclerView) {

        db.collection("Payments")
            .orderBy("timestamp") // Sort by latest
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.isEmpty) {
                    Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val list = mutableListOf<TransactionItem>()

                for (doc in snapshot.documents) {

                    val userName = doc.getString("name") ?: "Unknown"
                    val type = doc.getString("type") ?: "Donation"
                    val campaign = doc.getString("campaignName") ?: "N/A"
                    val amount = doc.get("amount").toString()

                    list.add(

                        TransactionItem(
                            userName = userName,
                            type = type,
                            campaign = campaign,
                            amount = "₹$amount"
                        )
                    )
                }

                recyclerView.adapter = TransactionAdapter(list)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading transactions", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchSpecificUserTransactions(recyclerView: RecyclerView) {
        val pref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val email = pref.getString("email", "") ?: ""

        if (email.isEmpty()) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("Payments")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.isEmpty) {
                    Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val list = mutableListOf<TransactionItem>()

                for (doc in snapshot.documents) {

                    val userName = doc.getString("name") ?: "Unknown"
                    val type = doc.getString("type") ?: "Donation"
                    val campaign = doc.getString("campaignName") ?: "N/A"
                    val amount = doc.get("amount").toString()

                    list.add(
                        TransactionItem(
                            userName = userName,
                            type = type,
                            campaign = campaign,
                            amount = "₹$amount"
                        )
                    )
                }

                recyclerView.adapter = TransactionAdapter(list)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading transactions", Toast.LENGTH_SHORT).show()
            }
    }

}
