package com.example.messengerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.messengerapp.service_locator.AppContainer
import com.example.messengerapp.ui.BaseViewModelFactory
import com.example.messengerapp.ui.chat.ChatScreen
import com.example.messengerapp.ui.chat.ChatViewModel
import com.example.messengerapp.ui.home.HomeScreen
import com.example.messengerapp.ui.home.HomeViewModel
import com.example.messengerapp.ui.login.LoginScreen
import com.example.messengerapp.ui.profile.ProfileScreen
import com.example.messengerapp.ui.register.RegisterScreen
import com.example.messengerapp.ui.search.SearchScreen
import com.example.messengerapp.ui.search.SearchViewModel

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.LOGIN,
        modifier = modifier
    ) {
        composable(AppRoute.LOGIN) {
            LoginScreen(navController)
        }
        composable(AppRoute.REGISTER) {
            RegisterScreen(navController)
        }
        composable(AppRoute.HOME) {
            val currentUid = AppContainer.firebaseAuth.currentUser?.uid.orEmpty()

            val homeViewModelFactory = BaseViewModelFactory {
                HomeViewModel(
                    currentUid,
                    AppContainer.getAllChatRoomsUseCase
                )
            }

            val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
            HomeScreen(navController, homeViewModel)
        }

        composable(AppRoute.PROFILE) {
            ProfileScreen(navController)
        }

        composable(
            route = AppRoute.CHAT_WITH_ID,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val currentUserUid = AppContainer.firebaseAuth.currentUser?.uid.orEmpty()

            val chatViewModelFactory = BaseViewModelFactory {
                ChatViewModel(
                    currentUserId = currentUserUid,
                    sendMessageUseCase = AppContainer.sendMessageUseCase,
                    observeMessageUseCase = AppContainer.observeMessageUseCase,
                    getUserByUidUseCase = AppContainer.getUserByUidUseCase
                )
            }

            val chatViewModel: ChatViewModel = viewModel(factory = chatViewModelFactory)

            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(navController, chatId, chatViewModel)
        }

        composable(AppRoute.SEARCH) {
            val currentUid = AppContainer.firebaseAuth.currentUser?.uid.orEmpty()

            val viewModelFactory = BaseViewModelFactory {
                SearchViewModel(
                    currentUid,
                    AppContainer.getOrCreateChatRoomUseCase,
                    AppContainer.searchUserUseCase
                )
            }

            val searchViewModel: SearchViewModel = viewModel(factory = viewModelFactory)
            SearchScreen(navController, searchViewModel)
        }
    }

}