package com.example.messengerapp.domain.repository

import com.example.messengerapp.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MessageHandleRepository {
    suspend fun sendMessage (mess: Message) : Result<Unit>

    fun observeMessages (chatId : String) : Flow<List<Message>>
}