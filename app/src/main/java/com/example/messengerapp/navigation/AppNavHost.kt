package com.example.messengerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.messengerapp.ui.home.HomeScreen
import com.example.messengerapp.ui.chat.ChatScreen
import com.example.messengerapp.ui.login.LoginScreen
import com.example.messengerapp.ui.profile.ProfileScreen
import com.example.messengerapp.ui.register.RegisterScreen
import com.example.messengerapp.ui.search.SearchScreen

@Composable
fun AppNavHost (navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.LOGIN,
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
        composable (
            route = AppRoute.CHAT_WITH_ID,
            arguments = listOf(navArgument ("chatId"){type = NavType.StringType })
        ){  backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")?: ""
            ChatScreen(navController, chatId)
        }
        composable (AppRoute.SEARCH){
            SearchScreen(navController)
        }
    }

}