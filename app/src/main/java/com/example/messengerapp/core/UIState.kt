package com.example.messengerapp.core

sealed class UIState {
    object Idle : UIState()
    object Loading : UIState()
    data class Success <out T> (val data: T): UIState()
    data class Error(val message: String): UIState()
}