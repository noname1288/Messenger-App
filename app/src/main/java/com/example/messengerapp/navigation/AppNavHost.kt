package com.example.messengerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.messengerapp.ui.home.HomeScreen
import com.example.messengerapp.ui.login.AuthViewModel
import com.example.messengerapp.ui.login.LoginScreen
import com.example.messengerapp.ui.register.RegisterScreen

@Composable
fun AppNavHost (navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.HOME,
        modifier = modifier
    ){
        composable (AppRoute.LOGIN){
            LoginScreen(navController, authViewModel = AuthViewModel())
        }
        composable (AppRoute.REGISTER){
            RegisterScreen(navController, authViewModel = AuthViewModel())
        }
        composable (AppRoute.HOME){
            HomeScreen(navController, authViewModel = AuthViewModel())
        }
    }

}