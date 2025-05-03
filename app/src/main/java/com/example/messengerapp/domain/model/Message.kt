package com.example.messengerapp.domain.model

data class Message(
    val id: String = "",
    val chatId: String = "",          // uidA_uidB (sorted)
    val senderId: String = "",
    val receiverId: String = "",
    val text: String? = null,
    val photoUri: String? = null,
    val photoMimeType: String? = null,
    val timestamp: Long = System.currentTimeMillis()

)
