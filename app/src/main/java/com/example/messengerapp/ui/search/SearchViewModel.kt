package com.example.messengerapp.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.model.mockChatRoom
import com.example.messengerapp.domain.usecase.GetOrCreateChatRoomUseCase
import com.example.messengerapp.domain.usecase.SearchUserUseCase
import com.example.messengerapp.service_locator.AppContainer
import kotlinx.coroutines.launch

class SearchViewModel(
    private val currentUserUid: String,
    private val getOrCreateChatRoomUseCase: GetOrCreateChatRoomUseCase,
    private val  searchUserUseCase : SearchUserUseCase
) : ViewModel() {
    val currentUser = currentUserUid
    //search List<user> state
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

            val result = getOrCreateChatRoomUseCase(myId = currentUserUid, partner =  targetUser)
            Log.d("SearchViewModel", "currentUID: $currentUserUid ; targetUser: ${targetUser.uid}")

            _accessChatRoomState.value = result.fold(
                onSuccess = { chatRoom -> UIState.Success(chatRoom) },
                onFailure = { UIState.Error(it.message ?: "Lỗi tạo hoặc truy cập chat") }
            )

//            Log.d("SearchViewModel", "accessChatWithUser: $result")
        }
    }


    //xu ly state boi vi
    /**
     * 👉 viewModel() mặc định scope là NavBackStackEntry của AppRoute.SEARCH.
     * Nếu SEARCH chưa bị dispose hẳn (do popBackStack không đi đủ),
     * accessChatRoom vẫn còn giữ UIState.Success trước đó → gây navigateWithArgs("CHAT/%s") lặp lại sau khi Login thành công.
     *
     *
     */

    fun resetAccessState() {
        _accessChatRoomState.value = UIState.Initial
    }

}