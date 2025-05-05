package com.example.messengerapp.domain.usecase

import android.util.Log
import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.ChatRoomRepository
import com.example.messengerapp.service_locator.AppContainer

class GetOrCreateChatRoomUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(myId: String, partner: User): Result<ChatRoom> {
        //todo session management
        val currentUserName = ""
        val currentUserAvatar = ""

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

        Log.d("GetOrCreateChatRoomUseCase", "newRoom: $newRoom")
        return if (myId != partner.uid) chatRoomRepository.createChatRoom(
            chatRoom = newRoom,
            currentUserId = myId,
            currentUserName = currentUserName,
            currentUserAvatar = currentUserAvatar
        ) else Result.failure(
            Exception("Cannot create chat room")
        )
    }

    private fun generateChatId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) uid1 + "_" + uid2 else uid2 + "_" + uid1
    }
}
