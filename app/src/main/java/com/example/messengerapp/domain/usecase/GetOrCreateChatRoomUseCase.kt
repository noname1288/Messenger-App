package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.ChatRoomRepository

class GetOrCreateChatRoomUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(myId: String, partner: User): Result<ChatRoom> {
        val chatId = generateChatId(myId, partner.uid)

        val existingResult = chatRoomRepository.getChatRoom(chatId)
        if (existingResult.isSuccess) {
            return existingResult
        }

        val newRoom = ChatRoom(
            chatId = chatId,
            partnerId = partner.uid,
            partnerName = partner.displayName,
            partnerAvatar = partner.avatarUrl,
            lastMessage = "",
            lastTimestamp = System.currentTimeMillis()
        )

        return chatRoomRepository.createChatRoom(newRoom)
    }

    private fun generateChatId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) uid1+"_"+uid2 else uid2+"_"+uid1
    }
}
