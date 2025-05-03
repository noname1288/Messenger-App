package com.example.messengerapp.service_locator

import com.google.firebase.auth.FirebaseAuth

object AppContainer {
    // Firebase instance
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}