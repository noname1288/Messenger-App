package com.example.messengerapp.ui.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messengerapp.service_locator.AppContainer
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private var auth : FirebaseAuth = AppContainer.firebaseAuth

    private var _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String){
        _authState.value = AuthState.Loading

        if (email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
        }
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something were wrong")
                }
            }
        }catch (e : Exception){
            Log.e("AuthViewModel", "login: ${e.message}")
        }
    }

    fun signup (email: String, password: String){
        _authState.value = AuthState.Loading

        if (email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
        }
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something were wrong")
                }
            }
        }catch (e : Exception){
            Log.e("AuthViewModel", "sign up: ${e.message}")
        }
    }

    fun logout(){
        try{
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
        }catch (e: Exception){
            Log.e("AuthViewModel", "logout: ${e.message}")
        }
    }

    fun validateCredentials(email: String, password: String): String? {
        if (email.isBlank() || password.isBlank()) {
            return "Email and password cannot be empty"
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email format, must be @email.com"
        }

        if (password.length < 6) {
            return "Password must be at least 6 characters"
        }

        return null // ✅ Không có lỗi
    }

}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}