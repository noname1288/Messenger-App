package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.repository.UserRepository

class CreateNewUserUsecase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return userRepository.createUser(user)
    }
}