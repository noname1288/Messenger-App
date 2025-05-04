package com.example.messengerapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.service_locator.AppContainer
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val createNewUserUsecase = AppContainer.createNewUserUsecase

    private val _profileState = MutableLiveData<UIState<User>>(UIState.Initial)
    val profileState: LiveData<UIState<User>> = _profileState

    //create new user's profile
    fun createUserProfile (user: User){
        viewModelScope.launch {
            _profileState.value = UIState.Loading
            val result = createNewUserUsecase(user)
            Log.d("ProfileViewModel", "createUserProfile: $result")
            if (result.isSuccess){
                _profileState.value = UIState.Success(user)
            }else{
                UIState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

}