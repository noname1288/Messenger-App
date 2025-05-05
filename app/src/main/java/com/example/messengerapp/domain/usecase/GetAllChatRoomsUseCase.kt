package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow

class GetAllChatRoomsUseCase(private val chatRoomRepository: ChatRoomRepository) {
    operator fun invoke(userId: String): Flow<List<ChatRoom>> {
        return chatRoomRepository.observeChatRooms(userId)
    }
}