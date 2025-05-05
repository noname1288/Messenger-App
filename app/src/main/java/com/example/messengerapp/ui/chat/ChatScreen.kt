package com.example.messengerapp.ui.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.service_locator.AppContainer
import com.example.messengerapp.ui.BaseViewModelFactory
import com.example.messengerapp.utils.toFriendlyTimeString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, chatId: String, chatViewModel: ChatViewModel) {
    val context = LocalContext.current


    var textType by remember { mutableStateOf("") }

    //fetch data
    val messages by chatViewModel.messages.collectAsState()
    val sendState by chatViewModel.sendState.collectAsState()
    val listState = rememberLazyListState() //state danh sách tin nhắn -> Muc dich
    // Tự Scroll xuống cuối khi có tin nhắn mới

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    //theo doi neu gui loi
    LaunchedEffect(sendState) {
        when(sendState){
            is UIState.Error -> {
                Toast.makeText(context, (sendState as UIState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    LaunchedEffect(chatId) {
        chatViewModel.listenToMessages(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chat $chatId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
            )
        },
        bottomBar = {
            SendMessageBar(onSend = {
                textType = it
//                Log.d("ChatText", textType)

                chatViewModel.sendMessage(chatId, textType)
            })
        }
    ) { innerPadding ->
        // Danh sách tin nhắn
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            LazyColumn(
                state = listState,
                reverseLayout = false,
                modifier = Modifier.fillMaxSize()) {
                itemsIndexed(messages) { index, message ->
                    ChatBubble(msg = message, currentUser = chatViewModel.currentUser)
                }
            }
        }
    }
}


/* Bong bóng tin nhắn */
@Composable
fun ChatBubble(msg: Message, currentUser: String) {

    val bubbleColor = if (msg.senderId == currentUser) Color(0xFF906CF2) else Color.White
    val textColor = if (msg.senderId ==currentUser) Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (msg.senderId == currentUser) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .widthIn(max = 300.dp)
                .background(bubbleColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            if (!msg.text.isNullOrBlank()) {
                Text(msg.text, color = textColor)
                Spacer(Modifier.height(1.dp))
            }

            Text(
                text = msg.timestamp.toFriendlyTimeString(),
                fontSize = 10.sp,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}


/* Thanh soạn tin nhắn */
@Composable
fun SendMessageBar(onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        // Nút gửi
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    if (text.isNotBlank()) {
                        onSend(text)
                        text = ""
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
        }
    }
}

