package com.example.moneydonation.model

data class TransactionItem(
    val name: String,
    val type: String, // "Donation" or "Payout"
    val category: String,
    val amount: String
)
