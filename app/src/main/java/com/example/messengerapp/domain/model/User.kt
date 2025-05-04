package com.example.messengerapp.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val fcmToken: String = "",
    val createdAt: Long = System.currentTimeMillis()

)
