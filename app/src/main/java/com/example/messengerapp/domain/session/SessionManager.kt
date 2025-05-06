package com.example.messengerapp.domain.session

import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.UserRepository

object SessionManager {
    var currentUser : User? = null

    suspend fun fetch(uid: String, repo: UserRepository): Result<User> {
        val result = repo.getUserByUid(uid)
        result.onSuccess { currentUser = it }
        return result
    }
}