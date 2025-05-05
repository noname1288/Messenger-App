package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.domain.repository.MessageHandleRepository

class SendMessageUseCase (private val messageHandleRepository: MessageHandleRepository) {
    suspend operator fun invoke(message: Message): Result<Unit>{
        return messageHandleRepository.sendMessage(message)
    }

}