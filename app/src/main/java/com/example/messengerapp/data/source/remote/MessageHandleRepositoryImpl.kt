package com.example.messengerapp.data.source.remote

import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.domain.repository.MessageHandleRepository
import com.example.messengerapp.service_locator.AppContainer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageHandleRepositoryImpl (private val fireStore: FirebaseFirestore) : MessageHandleRepository {
    override suspend fun sendMessage(mess: Message): Result<Unit> {
        return try {
            val currentUserUid = AppContainer.firebaseAuth.currentUser?.uid.orEmpty()

            val chatRef = fireStore.collection("chats").document(mess.chatId) //sender

            val messageRef = chatRef.collection("messages").document()
            val finalMessage = mess.copy(id = messageRef.id)

            val inboxRef = fireStore.collection("inbox") //sender
                .document(currentUserUid)
                .collection("rooms")
                .document(mess.chatId)
            val receiverInboxRef = fireStore.collection("inbox") //receiver
                .document(mess.receiverId)
                .collection("rooms")
                .document(mess.chatId)

            val batch = fireStore.batch()
            //add message to chat
            batch.set(messageRef, finalMessage)

            //update last message in collection chats
            val chatUpdate = mapOf(
                "lastMessage" to (finalMessage.text ?: "[Photo]"),
                "lastMessageTimestamp" to finalMessage.timestamp,
                "participants" to listOf(mess.senderId, mess.receiverId)
            )

            //update last message in inbox
            val inboxUpdate = mapOf(
                "lastMessage" to (finalMessage.text ?: "[Photo]"),
                "lastMessageTimestamp" to finalMessage.timestamp
            )


            batch.set(chatRef,chatUpdate, SetOptions.merge())
            batch.set(inboxRef,inboxUpdate, SetOptions.merge())
            batch.set(receiverInboxRef, inboxUpdate, SetOptions.merge())


            batch.commit().await()
            Result.success(Unit)

        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = fireStore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.toObjects(Message::class.java) ?: emptyList()

                trySend(messages)
            }
        awaitClose { listener.remove() }
    }
}