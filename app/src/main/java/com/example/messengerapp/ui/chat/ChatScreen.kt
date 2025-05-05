package com.example.messengerapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messengerapp.domain.model.Message
import com.example.messengerapp.utils.toFriendlyTimeString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, chatId: String) {
    val chatViewModel: ChatViewModel = viewModel()

    //fake data
    val messages = chatViewModel.messageList

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
            SendMessageBar(onSend = {})
        }
    ) { innerPadding ->
        // Danh sách tin nhắn
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed (items = messages){index, message->
                    ChatBubble(msg = message)

                }
            }
        }
    }
}


/* Bong bóng tin nhắn */
@Composable
fun ChatBubble(msg: Message) {

    val bubbleColor = if (msg.senderId == "userA") Color(0xFF906CF2) else Color.White
    val textColor = if (msg.senderId == "userA") Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (msg.senderId == "userA") Alignment.End else Alignment.Start
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

            Text(text = msg.timestamp.toFriendlyTimeString(), fontSize = 10.sp, color = textColor.copy(alpha = 0.7f))
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