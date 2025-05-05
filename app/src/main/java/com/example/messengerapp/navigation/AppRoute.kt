package com.example.messengerapp.navigation

object AppRoute {
    val LOGIN = "LOGIN"
    val HOME = "HOME"
    val REGISTER = "REGISTER"
    val PROFILE ="PROFILE"
    const val CHAT = "CHAT"
    const val CHAT_WITH_ID = "$CHAT/{chatId}" // 👈 route có argument
    val SEARCH = "SEARCH"
}