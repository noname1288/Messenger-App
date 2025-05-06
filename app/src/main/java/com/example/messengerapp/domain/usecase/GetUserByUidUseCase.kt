package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.UserRepository

class GetUserByUidUseCase (private val userRepository: UserRepository) {
    suspend operator fun invoke(uid: String): Result<User> {
        return userRepository.getUserByUid(uid)
    }
}