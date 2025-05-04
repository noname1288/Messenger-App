package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.model.User

interface UserRepository {
    suspend fun createUser(user: User) : Result<Unit>


}