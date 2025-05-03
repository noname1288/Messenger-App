package com.example.messengerapp.domain.model

data class ChatRoom(               // để hiển thị “recent chat”
    val chatId: String = "",
    val partnerId: String = "",
    val partnerName: String = "",
    val partnerAvatar: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0
)

val mockChatRoom = listOf(
    ChatRoom(
        chatId = "user123_user001",
        partnerId = "user001",
        partnerName = "Alice Johnson",
        partnerAvatar = "https://randomuser.me/api/portraits/women/1.jpg",
        lastMessage = "Hey, how are you?",
        lastTimestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000 // 2 giờ trước
    ),
    ChatRoom(
        chatId = "user123_user002",
        partnerId = "user002",
        partnerName = "Bob Smith",
        partnerAvatar = "https://randomuser.me/api/portraits/men/2.jpg",
        lastMessage = "See you tomorrow!",
        lastTimestamp = System.currentTimeMillis() - 10 * 60 * 1000 // 10 phút trước
    ),
    ChatRoom(
        chatId = "user123_user003",
        partnerId = "user003",
        partnerName = "Catherine Lee",
        partnerAvatar = "https://randomuser.me/api/portraits/women/3.jpg",
        lastMessage = "Let’s catch up later.",
        lastTimestamp = System.currentTimeMillis() - 5 * 60 * 1000
    ),
    ChatRoom(
        chatId = "user123_user004",
        partnerId = "user004",
        partnerName = "Daniel Park",
        partnerAvatar = "https://randomuser.me/api/portraits/men/4.jpg",
        lastMessage = "👍",
        lastTimestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000 // hôm qua
    ),
    ChatRoom(
        chatId = "user123_user005",
        partnerId = "user005",
        partnerName = "Emily Nguyen",
        partnerAvatar = "https://randomuser.me/api/portraits/women/5.jpg",
        lastMessage = "I'm on my way.",
        lastTimestamp = System.currentTimeMillis() - 15 * 60 * 1000
    ),
    ChatRoom(
        chatId = "user123_user006",
        partnerId = "user006",
        partnerName = "Frank Wilson",
        partnerAvatar = "https://randomuser.me/api/portraits/men/6.jpg",
        lastMessage = "Thanks for the update.",
        lastTimestamp = System.currentTimeMillis() - 45 * 60 * 1000
    ),
    ChatRoom(
        chatId = "user123_user007",
        partnerId = "user007",
        partnerName = "Grace Liu",
        partnerAvatar = "https://randomuser.me/api/portraits/women/7.jpg",
        lastMessage = "Let me know when you're free.",
        lastTimestamp = System.currentTimeMillis() - 6 * 60 * 60 * 1000
    ),
    ChatRoom(
        chatId = "user123_user008",
        partnerId = "user008",
        partnerName = "Henry Adams",
        partnerAvatar = "https://randomuser.me/api/portraits/men/8.jpg",
        lastMessage = "LOL 😄",
        lastTimestamp = System.currentTimeMillis() - 30 * 60 * 1000
    ),
    ChatRoom(
        chatId = "user123_user009",
        partnerId = "user009",
        partnerName = "Isabelle Kim",
        partnerAvatar = "https://randomuser.me/api/portraits/women/9.jpg",
        lastMessage = "Call me later?",
        lastTimestamp = System.currentTimeMillis() - 1 * 60 * 60 * 1000
    ),
    ChatRoom(
        chatId = "user123_user010",
        partnerId = "user010",
        partnerName = "Jack Ma",
        partnerAvatar = "https://randomuser.me/api/portraits/men/10.jpg",
        lastMessage = "Got it. Thanks!",
        lastTimestamp = System.currentTimeMillis() - 3 * 60 * 60 * 1000
    )
)
