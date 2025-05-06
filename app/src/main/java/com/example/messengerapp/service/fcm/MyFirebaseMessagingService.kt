package com.example.messengerapp.service.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.messengerapp.MainActivity
import com.example.messengerapp.R
import com.example.messengerapp.service_locator.AppContainer
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Khi có tin nhắn tới
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM", "message.data = ${message.data}")

        val chatId = message.data["chatId"] ?: return
        val senderId = message.data["senderId"] ?: ""
        val senderName = message.data["senderName"] ?: "Người gửi"
        val body = message.data["body"] ?: "Tin nhắn mới"

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("chatId", chatId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "chat_channel")
            .setSmallIcon(R.drawable.ic_notifications_active_24)
            .setContentTitle(senderName)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(chatId.hashCode(), notification)
    }

    // Khi token mới được tạo hoặc làm mới
    override fun onNewToken(newToken: String) {
        Log.d("FCM", "onNewToken: $newToken")

        val uid = AppContainer.firebaseAuth.currentUser?.uid ?: return
        val db = AppContainer.firestore

        val userRef = db.collection("users").document(uid)

        // Lưu theo dạng array (1 user nhiều thiết bị)
        userRef.set(
            mapOf("deviceTokens" to listOf(newToken)),
            SetOptions.merge()
        )
    }

    companion object {
        // Hàm gọi khi login thành công lần đầu
        fun uploadTokenIfNeeded() {
            val auth = AppContainer.firebaseAuth
            val uid = auth.currentUser?.uid ?: return
            val db = AppContainer.firestore
            val userRef = db.collection("users").document(uid)

            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    // Lưu nếu chưa có
                    userRef.get().addOnSuccessListener { doc ->
                        val existingTokens = doc.get("deviceTokens") as? List<*> ?: emptyList<Any>()
                        if (!existingTokens.contains(token)) {
                            userRef.set(
                                mapOf("deviceTokens" to com.google.firebase.firestore.FieldValue.arrayUnion(token)),
                                SetOptions.merge()
                            )
                        }
                    }
                }
        }

        fun deleteTokenOnLogout() {
            FirebaseMessaging.getInstance().deleteToken()
                .addOnSuccessListener { Log.d("FCM", "Token deleted on logout") }
        }
    }
}
