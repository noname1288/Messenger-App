package com.example.messengerapp.core

sealed class UIState<out T>{
    object Initial : UIState<Nothing>()
    object Authenticated : UIState<Nothing>()
    object Unauthenticated : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}