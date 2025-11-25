package com.example.moneydonation.model

data class User(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    val createdAt: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
)