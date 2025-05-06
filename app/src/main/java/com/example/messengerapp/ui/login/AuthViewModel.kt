package com.example.messengerapp.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messengerapp.core.UIState
import com.example.messengerapp.data.source.remote.UserRepositoryImpl
import com.example.messengerapp.domain.repository.UserRepository
import com.example.messengerapp.domain.session.SessionManager
import com.example.messengerapp.service_locator.AppContainer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = AppContainer.firebaseAuth
    private var userRepository: UserRepositoryImpl = AppContainer.userRepository

    private var _signInState = MutableLiveData<UIState<Unit>>(UIState.Initial)
    val signInState: LiveData<UIState<Unit>> = _signInState

    init {
        checkAuthStatus()
    }

    //
    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _signInState.value = UIState.Unauthenticated
        } else {
            _signInState.value = UIState.Authenticated
        }
    }

    //User login
    fun login(email: String, password: String) {
        _signInState.value = UIState.Loading

        if (email.isBlank() || password.isBlank()) {
            _signInState.value = UIState.Error("Email and password cannot be empty")
            return
        }

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signInState.value = UIState.Authenticated
                } else {
                    val message = when (val e = task.exception) {
                        is FirebaseAuthInvalidUserException -> "Tài khoản không tồn tại"
                        is FirebaseAuthInvalidCredentialsException -> "Sai tài khoản hoặc mật khẩu"
                        else -> e?.localizedMessage ?: "Đăng nhập thất bại"
                    }
                    _signInState.value = UIState.Error(message)
                }
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "login: ${e.message}")
        }
    }

    //signup user with email and password
    fun signup(email: String, password: String) {
        _signInState.value = UIState.Loading

        if (email.isBlank() || password.isBlank()) {
            _signInState.value = UIState.Error("Email và mật khẩu không được để trống")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signInState.value = UIState.Error("Email không hợp lệ")
            return
        }

        if (password.length < 6) {
            _signInState.value = UIState.Error("Mật khẩu phải có ít nhất 6 ký tự")
            return
        }

        try {
            auth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signInState.value = UIState.Authenticated
                    } else {
                        val errorMsg = when (val ex = task.exception) {
                            is FirebaseAuthUserCollisionException -> "Email đã được đăng ký"
                            is FirebaseAuthWeakPasswordException -> "Mật khẩu quá yếu"
                            else -> ex?.localizedMessage ?: "Đăng ký thất bại"
                        }
                        _signInState.value = UIState.Error(errorMsg)
                    }
                }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "sign up: ${e.message}")
        }
    }

    //logout user
    fun logout() {
        try {
            _signInState.value = UIState.Unauthenticated
            SessionManager.currentUser = null
            auth.signOut()
        } catch (e: Exception) {
            Log.e("AuthViewModel", "logout: ${e.message}")
        }
    }


}