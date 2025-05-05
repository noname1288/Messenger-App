package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.model.User

interface UserRepository {
    //create new user
    suspend fun createUser(user: User) : Result<Unit>

    //search users
    suspend fun searchUser(keyword: String) : Result<List<User>>
}