package com.example.messengerapp.service_locator

import com.example.messengerapp.data.source.remote.ChatRoomRepositoryImpl
import com.example.messengerapp.data.source.remote.MessageHandleRepositoryImpl
import com.example.messengerapp.data.source.remote.UserRepositoryImpl
import com.example.messengerapp.domain.usecase.CreateNewUserUsecase
import com.example.messengerapp.domain.usecase.GetAllChatRoomsUseCase
import com.example.messengerapp.domain.usecase.GetOrCreateChatRoomUseCase
import com.example.messengerapp.domain.usecase.ObserveMesssageUseCase
import com.example.messengerapp.domain.usecase.SearchUserUseCase
import com.example.messengerapp.domain.usecase.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AppContainer {

    /************
    Firebase instance
    ****************/
    //auth
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    //firestore
    val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    /************
     Rest of App
     ****************/

//    //current User
//    val currentUserUid: String
//        get() = firebaseAuth.currentUser?.uid ?: ""

    //repository
    private  val userRepositoryImpl by lazy {
        UserRepositoryImpl(firestore)
    }
    private val chatRoomRepositoryImpl by lazy {
        ChatRoomRepositoryImpl(firestore)
    }
    private val messageHandleRepositoryImpl by lazy {
        MessageHandleRepositoryImpl(firestore)
    }


    //usecase
    val createNewUserUsecase by lazy {
        CreateNewUserUsecase(userRepositoryImpl)
    }
    val getOrCreateChatRoomUseCase by lazy {
        GetOrCreateChatRoomUseCase(chatRoomRepositoryImpl)
    }
    val searchUserUseCase by lazy {
        SearchUserUseCase(userRepositoryImpl)
    }
    val sendMessageUseCase by lazy {
        SendMessageUseCase(messageHandleRepositoryImpl)
    }
    val observeMessageUseCase by lazy {
        ObserveMesssageUseCase(messageHandleRepositoryImpl)
    }
    val getAllChatRoomsUseCase by lazy {
        GetAllChatRoomsUseCase(chatRoomRepositoryImpl)
    }


}