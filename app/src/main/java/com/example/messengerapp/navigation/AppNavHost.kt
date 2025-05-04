package com.example.messengerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.messengerapp.ui.home.HomeScreen
import com.example.messengerapp.ui.home.HomeViewModel
import com.example.messengerapp.ui.AuthViewModel
import com.example.messengerapp.ui.chat.ChatScreen
import com.example.messengerapp.ui.login.LoginScreen
import com.example.messengerapp.ui.profile.ProfileScreen
import com.example.messengerapp.ui.register.RegisterScreen

@Composable
fun AppNavHost (navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.CHAT,
        modifier = modifier
    ){
        composable (AppRoute.LOGIN){
            LoginScreen(navController)
        }
        composable (AppRoute.REGISTER){
            RegisterScreen(navController)
        }
        composable (AppRoute.HOME){
            HomeScreen(navController)
        }
        composable (AppRoute.PROFILE){
            ProfileScreen(navController)
        }
        composable (AppRoute.CHAT){
            ChatScreen(navController)
        }
    }

}