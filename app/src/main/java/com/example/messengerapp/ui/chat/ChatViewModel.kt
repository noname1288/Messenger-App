package com.example.messengerapp.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.usecase.GetUserByUidUseCase
import com.example.messengerapp.domain.usecase.ObserveMesssageUseCase
import com.example.messengerapp.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val currentUserId: String,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessageUseCase: ObserveMesssageUseCase,
    private val getUserByUidUseCase: GetUserByUidUseCase
) : ViewModel() {
    val currentUser = currentUserId

    //get partner
    private val _partner = MutableStateFlow<User?>(null)
    val partner: StateFlow<User?> = _partner

    //sendState
    private val _sendState = MutableStateFlow<UIState<Unit>>(UIState.Initial)
    val sendState: StateFlow<UIState<Unit>> = _sendState

    //observeMessage
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    //event
    //get partner Info
    fun getPartnerInfo(partnerId: String) {
        viewModelScope.launch {
            val result = getUserByUidUseCase(partnerId)
            _partner.value = result.fold(
                onSuccess = { it },
                onFailure = {
                    Log.e("ChatViewModel", "Lỗi lấy thông tin partner: ${it.message}")
                    null // bạn có thể dùng default User khác nếu muốn
                }
            )
        }
    }

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

    //lay receiverID
    fun extractReceiverId(chatId: String, currentUserId: String): String {
        val ids = chatId.split("_")
        return if (ids[0] == currentUserId) ids[1] else ids[0]
    }
}

