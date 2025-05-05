package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.model.ChatRoom

interface ChatRoomRepository {
    suspend fun getChatRoom(chatId: String): Result<ChatRoom>

    suspend fun createChatRoom(chatRoom: ChatRoom): Result<ChatRoom>

}