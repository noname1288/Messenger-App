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

val mockMessageList = listOf(
    Message(
        id = "msg1",
        chatId = "userA_userB",
        senderId = "userA",
        receiverId = "userB",
        text = "Hello!",
        timestamp = System.currentTimeMillis() - 10*60*1000
    ),
    Message(
        id = "msg2",
        chatId = "userA_userB",
        senderId = "userB",
        receiverId = "userA",
        text = "Hi there!",
        timestamp = System.currentTimeMillis() - 9*60*1000
    ),
    Message(
        id = "msg3",
        chatId = "userA_userB",
        senderId = "userA",
        receiverId = "userB",
        text = "How are you?",
        timestamp = System.currentTimeMillis() - 8*60*1000
    ),
    Message(
        id = "msg4",
        chatId = "userA_userB",
        senderId = "userB",
        receiverId = "userA",
        text = "I'm good, thanks!",
        timestamp = System.currentTimeMillis() - 7*60*1000
    ),
    Message(
        id = "msg5",
        chatId = "userA_userB",
        senderId = "userA",
        receiverId = "userB",
        text = "Wanna meet today?",
        timestamp = System.currentTimeMillis() - 6*60*1000
    ),
    Message(
        id = "msg6",
        chatId = "userA_userB",
        senderId = "userB",
        receiverId = "userA",
        text = "Sure, what time?",
        timestamp = System.currentTimeMillis() - 5*60*1000
    ),
    Message(
        id = "msg7",
        chatId = "userA_userB",
        senderId = "userA",
        receiverId = "userB",
        text = "Around 3 PM?",
        timestamp = System.currentTimeMillis() - 4*60*1000
    ),
    Message(
        id = "msg8",
        chatId = "userA_userB",
        senderId = "userB",
        receiverId = "userA",
        text = "Perfect!",
        timestamp = System.currentTimeMillis() - 3*60*1000
    ),
    Message(
        id = "msg9",
        chatId = "userA_userB",
        senderId = "userA",
        receiverId = "userB",
        photoUri = "uri",
        photoMimeType = "image/jpeg",
        text = "Check this out!",
        timestamp = System.currentTimeMillis() - 2*60*1000
    ),
    Message(
        id = "msg10",
        chatId = "userA_userB",
        senderId = "userB",
        receiverId = "userA",
        text = "Nice picture!",
        timestamp = System.currentTimeMillis() - 1*60*1000
    )
)
