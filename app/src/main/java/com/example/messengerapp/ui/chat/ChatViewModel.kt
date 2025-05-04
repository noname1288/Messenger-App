package com.example.messengerapp.ui.chat

import androidx.lifecycle.ViewModel
import com.example.messengerapp.domain.model.mockMessageList

class ChatViewModel : ViewModel() {
    val messageList = mockMessageList
}