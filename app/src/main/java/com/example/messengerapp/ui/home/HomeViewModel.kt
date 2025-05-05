package com.example.messengerapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.usecase.GetAllChatRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val currentUserUid: String,
    private val getAllChatRoomsUseCase: GetAllChatRoomsUseCase
) : ViewModel() {

    val currentUser = currentUserUid

    init {
        fetchChatRooms()
    }

    private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms

    //event: fetch ChatRooms
    fun fetchChatRooms() {
        viewModelScope.launch {
            getAllChatRoomsUseCase(currentUserUid).collect { rooms ->
                _chatRooms.value = rooms
            }
        }
    }
}