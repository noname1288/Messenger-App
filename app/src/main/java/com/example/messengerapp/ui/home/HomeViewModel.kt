package com.example.messengerapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.messengerapp.domain.model.mockChatRoom

class HomeViewModel :ViewModel() {
    val chatRooms = mockChatRoom
}