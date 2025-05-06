package com.example.messengerapp.data.source.remote

import android.util.Log
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.UserRepository
import com.example.messengerapp.ui.login.LoginScreen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val fireStore: FirebaseFirestore) : UserRepository {
    override suspend fun getUserByUid(uid: String): Result<User> {
         return try {
            val snapshot = fireStore.collection("users").document(uid).get().await()

            val currentUser = snapshot.toObject(User :: class.java)

             Log.d("UserRepositoryImpl", "getUserByUid: $currentUser")
             if (currentUser != null) {
                 Result.success(currentUser)
             } else {
                 Result.failure(Exception("User not found"))
             }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            // Lấy FCM token
            val token = FirebaseMessaging.getInstance().token.await()
            val userWithToken = user.copy(fcmToken = token)

            // Chuyển user thành map để thêm field "name_lowercase"
            val userMap = hashMapOf<String, Any>(
                "uid" to userWithToken.uid,
                "email" to userWithToken.email,
                "displayName" to userWithToken.displayName,
                "avatarUrl" to userWithToken.avatarUrl,
                "fcmToken" to userWithToken.fcmToken,
                "createdAt" to userWithToken.createdAt,
                "name_lowercase" to userWithToken.displayName.lowercase() // 👈 thêm trường tìm kiếm
            )

            fireStore.collection("users")
                .document(user.uid)
                .set(userMap)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUser(keyword: String): Result<List<User>> {
        return try {
            val queryLower = keyword.trim().lowercase()

            val snapshot = fireStore.collection("users")
                .whereGreaterThanOrEqualTo("name_lowercase", queryLower)
                .whereLessThanOrEqualTo("name_lowercase", queryLower + '\uf8ff')
                .get()
                .await()

            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
