package com.example.messengerapp.service_locator


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService


class MyService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("MyService", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }


}