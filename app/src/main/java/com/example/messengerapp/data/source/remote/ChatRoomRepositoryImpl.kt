package com.example.messengerapp.data.source.remote

import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.repository.ChatRoomRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ChatRoomRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val currentUserId: String
) : ChatRoomRepository {
    override suspend fun getChatRoom(chatId: String): Result<ChatRoom> {
        return try {
            val snapshot = fireStore.collection("inbox")
                .document(currentUserId) // document = user_uid
                .collection("rooms")
                .document(chatId)
                .get()
                .await()

            if (snapshot.exists()){
                val chatRoom = snapshot.toObject(ChatRoom :: class.java)
                if (chatRoom != null){
                    Result.success(chatRoom)
                }else {
                    Result.failure(Exception("Cannot parse ChatRoom"))
                }
            }else {
                Result.failure(Exception("ChatRoom does not exist"))
            }

        }catch (e : Exception){
            Result.failure(e)
        }
    }

    override suspend fun createChatRoom(chatRoom: ChatRoom): Result<ChatRoom> {
        return try {
            val batch = fireStore.batch()

            // Cho current user
            val currentUserRef = fireStore.collection("inbox")
                .document(currentUserId)
                .collection("rooms")
                .document(chatRoom.chatId)

            // Cho partner user
            val partnerRef = fireStore.collection("inbox")
                .document(chatRoom.partnerId)
                .collection("rooms")
                .document(chatRoom.chatId)

            batch.set(currentUserRef, chatRoom)
            batch.set(partnerRef, chatRoom) // Có thể tuỳ chỉnh thông tin nếu cần

            batch.commit().await()

            Result.success(chatRoom)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}