package com.example.messengerapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.messengerapp.domain.model.ChatRoom
import com.example.messengerapp.navigation.AppRoute
import com.example.messengerapp.navigation.safeNavigate
import com.example.messengerapp.ui.login.AuthViewModel
import com.example.messengerapp.ui.search.SearchViewModel
import com.example.messengerapp.utils.toFriendlyTimeString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, searchViewModel: SearchViewModel, homeViewModel: HomeViewModel) {
    val context = LocalContext.current

    var authViewModel: AuthViewModel = viewModel()

//    val searchState = searchViewModel.searchState.observeAsState()
    var showLoading by remember { mutableStateOf(false) }

    val chatRoomsList by homeViewModel.chatRooms.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Hello ${homeViewModel.currentUser}") },
            actions = {
                IconButton(onClick = { navController.navigate(AppRoute.SEARCH) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "icon search")
                }
                IconButton(onClick = {
                    authViewModel.logout()
                    navController.navigate(AppRoute.LOGIN) {
                        popUpTo(0) { inclusive = true } // Xóa toàn bộ stack
                    }
                }) {
                    Icon(Icons.Default.Output, contentDescription = null)
                }
            }
        )
    }) { innerpadding ->
        Box(modifier = Modifier.padding(innerpadding)) {
            LazyColumn {

                itemsIndexed(
                    items = chatRoomsList,
                    key = { _, chatRoom -> chatRoom.chatId } // optional, improves performance
                ) { index, chatRoom ->
                    ChatRowItem(
                        item = chatRoom,
                        onClick = {})
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}


@Composable
fun ChatRowItem(item: ChatRoom, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1) Avatar
        AvatarSection(partnerAvatar = item.partnerAvatar, partnerName = item.partnerName)
        // 2) Tên + LastMessage
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = item.partnerName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.lastMessage,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp
                ),
                maxLines = 1
            )
        }
        // 3) Thời gian + Badge
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.lastTimestamp.toFriendlyTimeString(),
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(4.dp))
//            if (item.unreadCount > 0) {
//                BadgeUnread(count = item.unreadCount)
//            }
        }

    }
}

@Composable
fun AvatarSection(
    partnerAvatar: String,
    partnerName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val initials = partnerName
        .trim()
        .split(" ")
        .take(2)
        .joinToString("") { it.firstOrNull()?.toString() ?: "" }
        .uppercase()

    val painter = rememberAsyncImagePainter(
        model = partnerAvatar.takeIf { it.isNotBlank() }
    )

    val painterState = painter.state
    val isError = painterState is AsyncImagePainter.State.Error || partnerAvatar.isBlank()

    if (isError) {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.ifEmpty { "?" },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.primary

                ),
                fontSize = 16.sp
            )
        }
    } else {
        Image(
            painter = painter,
            contentDescription = "Avatar of $partnerName",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    }
}