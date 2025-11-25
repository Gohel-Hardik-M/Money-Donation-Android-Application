package com.example.moneydonation.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneydonation.R
import com.example.moneydonation.adapter.NotificationAdapter
import com.example.moneydonation.model.NotificationItem
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class NotificationActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotifications)

        btnBack.setOnClickListener { finish() }

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchPaymentNotifications(recyclerView)
    }

    private fun fetchPaymentNotifications(recyclerView: RecyclerView) {

        db.collection("Payments")
            .orderBy("timestamp") // latest first
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.isEmpty) {
                    Toast.makeText(this, "No notifications found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val notificationList = mutableListOf<NotificationItem>()

                for (doc in snapshot.documents) {

                    val amount = doc.get("amount").toString()
                    val campaign = doc.get("campaignName").toString()
                    val status = doc.get("status").toString()
                    val time = doc.getLong("timestamp") ?: 0L

                    val readableTime = formatTimestamp(time)

                    val title = if (status == "success") "Donation Successful"
                    else "Payment Failed"

                    val message = "You donated ₹$amount to $campaign • $readableTime"

                    notificationList.add(
                        NotificationItem(
                            title = title,
                            message = message,
                            isRead = false
                        )
                    )
                }

                recyclerView.adapter = NotificationAdapter(notificationList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching notifications", Toast.LENGTH_SHORT).show()
            }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
