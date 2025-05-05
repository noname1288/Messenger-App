package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.UserRepository

class SearchUserUseCase (
    private val userRepository: UserRepository
){
    suspend operator fun invoke(keyword: String): Result<List<User>> {
        return userRepository.searchUser(keyword)
    }
}