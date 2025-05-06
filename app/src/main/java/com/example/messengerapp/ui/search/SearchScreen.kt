package com.example.messengerapp.ui.search

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messengerapp.core.UIState
import com.example.messengerapp.navigation.AppRoute
import com.example.messengerapp.navigation.navigateWithArgs
import com.example.messengerapp.navigation.popBackIfCan
import com.example.messengerapp.navigation.safeNavigate
import com.example.messengerapp.service_locator.AppContainer
import com.example.messengerapp.ui.BaseViewModelFactory
import com.example.messengerapp.ui.login.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel) {
    val context = LocalContext.current

    var query by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }

    val searchState = searchViewModel.searchUser.observeAsState()
    val accessState = searchViewModel.accessChatRoom.observeAsState()

    // Gọi tìm kiếm khi query thay đổi

    LaunchedEffect(searchState.value) {
        when (searchState.value) {
            is UIState.Loading -> showLoading = true
            is UIState.Error -> {
                showLoading = false
                Toast.makeText(context, (searchState.value as UIState.Error).message, Toast.LENGTH_SHORT).show()
            }
            is UIState.Success ->{
                showLoading = false
            }
            else -> Unit
        }
    }

    LaunchedEffect(accessState.value) {
        when (val state = accessState.value) {
            is UIState.Loading -> showLoading = true
            is UIState.Error -> {
                showLoading = false
                Toast.makeText(context, (accessState.value as UIState.Error).message, Toast.LENGTH_SHORT).show()
            }
            is UIState.Success ->{
                showLoading = false
                //navigate to chat Room
                val chatRoom = state.data
                navController.navigateWithArgs("CHAT/%s", chatRoom.chatId, popUpToRoute = AppRoute.SEARCH)
                searchViewModel.resetAccessState()
            }
            else -> Unit
        }
    }

    if (showLoading){
        LoadingDialog()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {Text("Search User")},
            navigationIcon = {IconButton(onClick = {
                navController.popBackIfCan()
            }) {Icon(Icons.Default.ArrowBackIosNew, contentDescription = null) }}
        )
    }) { innerPadding -> Column(
        Modifier
            .fillMaxSize()
            .padding(innerPadding).padding(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Tìm người dùng") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = {
                if (query.isNotBlank()) {
                    searchViewModel.searchUsers(query)
                }
            }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
        Spacer(Modifier.height(16.dp))
        // Hiển thị danh sách kết quả khi có
        val result = searchState.value
        if (result is UIState.Success) {
            LazyColumn {
                itemsIndexed (items = result.data) { index, user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                searchViewModel.accessChatWithUser(user)
                                Log.d("SearchScreen", "User clicked: $user")
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar placeholder
                        Box(
                            Modifier
                                .size(48.dp)
                                .background(Color.Gray, CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(user.displayName, fontSize = 16.sp)
                    }
                }
            }
        }
    }}

}