package com.example.messengerapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.model.mockChatRoom
import com.example.messengerapp.service_locator.AppContainer
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    val chatRooms = mockChatRoom

    //usecase
    private val getOrCreateChatRoomUseCase = AppContainer.getOrCreateChatRoomUseCase
    private val searchUserUseCase = AppContainer.searchUserUseCase

    //search List<user state
    private val _searchUserState = MutableLiveData<UIState<List<User>>>(UIState.Initial)
    val searchUser : MutableLiveData<UIState<List<User>>> = _searchUserState

    //access to chat room state
    private val _accessChatRoomState = MutableLiveData<UIState<ChatRoom>>(UIState.Initial)
    val accessChatRoom : MutableLiveData<UIState<ChatRoom>> = _accessChatRoomState

    //event
    //searchByWord
    fun searchUsers(keyword: String) {
        viewModelScope.launch {
            _searchUserState.value = UIState.Loading
            val result = searchUserUseCase(keyword)
            _searchUserState.value = result.fold(
                onSuccess = { UIState.Success(it) },
                onFailure = { UIState.Error(it.message ?: "Lỗi tìm kiếm") }
            )
        }
    }
    //Access chat room. If not exist, create new chat room
    fun accessChatWithUser(targetUser: User) {
        viewModelScope.launch {
            _accessChatRoomState.value = UIState.Loading

            val result = getOrCreateChatRoomUseCase(AppContainer.currentUserUid, targetUser)

            _accessChatRoomState.value = result.fold(
                onSuccess = { chatRoom -> UIState.Success(chatRoom) },
                onFailure = { UIState.Error(it.message ?: "Lỗi tạo hoặc truy cập chat") }
            )
        }
    }


}