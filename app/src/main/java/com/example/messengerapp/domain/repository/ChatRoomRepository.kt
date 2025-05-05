package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.model.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRoomRepository {
    suspend fun getChatRoom(chatId: String): Result<ChatRoom>

    suspend fun createChatRoom(
        currentUserId: String,
        currentUserName: String,
        currentUserAvatar: String, chatRoom: ChatRoom
    ): Result<ChatRoom>

    fun observeChatRooms(userId: String): Flow<List<ChatRoom>>

}