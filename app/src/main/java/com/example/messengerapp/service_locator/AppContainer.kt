package com.example.messengerapp.service_locator

import com.example.messengerapp.data.source.remote.UserRepositoryImpl
import com.example.messengerapp.domain.usecase.CreateNewUserUsecase
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

    //repository
    private  val userRepositoryImpl by lazy {
        UserRepositoryImpl(firestore)
    }
    //usecase
    val createNewUserUsecase by lazy {
        CreateNewUserUsecase(userRepositoryImpl)
    }
}