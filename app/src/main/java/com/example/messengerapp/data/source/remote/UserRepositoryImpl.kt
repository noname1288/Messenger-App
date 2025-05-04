package com.example.messengerapp.data.source.remote

import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.UserRepository
import com.example.messengerapp.service_locator.AppContainer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val fireStore : FirebaseFirestore) : UserRepository {

    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            // Lấy FCM token
            val token = FirebaseMessaging.getInstance().token.await()
            val userWithToken = user.copy(fcmToken = token)

            fireStore.collection("users")
                .document(user.uid)
                .set(userWithToken)
                .await()

            Result.success(Unit)

        }catch (e: Exception){
            Result.failure(e)
        }
    }
}