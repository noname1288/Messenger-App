package com.example.messengerapp.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.domain.usecase.ObserveMesssageUseCase
import com.example.messengerapp.domain.usecase.SendMessageUseCase
import com.example.messengerapp.service_locator.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val currentUserId: String,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessageUseCase: ObserveMesssageUseCase
) : ViewModel() {
    val currentUser = currentUserId
    //usecase


    //sendState
    private val _sendState = MutableStateFlow<UIState<Unit>>(UIState.Initial)
    val sendState: StateFlow<UIState<Unit>> = _sendState

    //observeMessage
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    //event
    //send message
    fun sendMessage(chatId: String, textInput: String) {
        val message = Message(
            chatId = chatId,
            senderId = currentUserId,
            receiverId = extractReceiverId(chatId, currentUserId),
            text = textInput
        )

        viewModelScope.launch {
            _sendState.value = UIState.Loading
            val result = sendMessageUseCase(message)
            _sendState.value = result.fold(
                onSuccess = { UIState.Success(Unit) },
                onFailure = { UIState.Error(it.message ?: "Lỗi gửi tin nhắn") }
            )
        }
//        Log.d("ChatViewModel", "Message sent: $message ; State: ${_sendState.value}")
    }

    //observe message
    fun listenToMessages(chatId: String) {
        viewModelScope.launch {
            observeMessageUseCase(chatId).collect {
                _messages.value = it
            }
        }
    }
}

//lay receiverID
fun extractReceiverId(chatId: String, currentUserId: String): String {
    val ids = chatId.split("_")
    return if (ids[0] == currentUserId) ids[1] else ids[0]
}