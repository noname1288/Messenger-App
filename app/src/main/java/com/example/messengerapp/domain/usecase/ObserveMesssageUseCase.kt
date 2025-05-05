package com.example.messengerapp.domain.usecase

import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.domain.repository.MessageHandleRepository
import kotlinx.coroutines.flow.Flow

class ObserveMesssageUseCase(private val repository: MessageHandleRepository) {
    operator fun invoke(chatId : String): Flow<List<Message>> {
        return repository.observeMessages(chatId)
    }
}