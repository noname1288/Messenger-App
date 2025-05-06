package com.example.messengerapp.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.model.User
import com.example.messengerapp.domain.session.SessionManager
import com.example.messengerapp.navigation.AppRoute
import com.example.messengerapp.navigation.navigateRoot
import com.example.messengerapp.navigation.safeNavigate
import com.example.messengerapp.service_locator.AppContainer
import com.example.messengerapp.ui.login.LoadingDialog

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current

    val profileViewModel: ProfileViewModel = viewModel()

    //get curentUser from register step
    val currentUser = AppContainer.firebaseAuth.currentUser

    val profileState = profileViewModel.profileState.observeAsState()
    var showLoading by remember { mutableStateOf(false) }
    var displayNameInput by remember { mutableStateOf("") }



    LaunchedEffect(profileState.value) {
        when (profileState.value) {
            is UIState.Loading -> showLoading = true
            is UIState.Success -> {
                val uid = AppContainer.firebaseAuth.currentUser?.uid.orEmpty()

                //  Gọi suspend function trong LaunchedEffect
                val result = SessionManager.fetch(uid, AppContainer.userRepository)

                if (result.isSuccess) {
                    Log.d("Session", "Lấy được user: ${SessionManager.currentUser}")
                    navController.navigateRoot(AppRoute.HOME) //  chỉ navigate khi đã fetch xong
                } else {
                    Toast.makeText(context, "Không lấy được thông tin user", Toast.LENGTH_SHORT).show()
                    Log.e("Session", "Không lấy được user: ${result.exceptionOrNull()?.message}")
                }
            }
            is UIState.Error -> Toast.makeText(
                context,
                (profileState.value as UIState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    if (showLoading) {
        LoadingDialog()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(85.dp))

        Text(text = "Profile", fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Welcome to messenger app",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )

        Spacer(Modifier.height(85.dp))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = displayNameInput,
            onValueChange = {displayNameInput = it},
            singleLine = true,
            label = { Text("Name") }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            singleLine = true,
            label = { Text("avatarUrl") }
        )

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White,
                )
            ) {
                Text("Back")
            }

            Button(onClick = {
                //fake data to create profile
                val userMock = User(
                    uid = currentUser?.uid.toString(),
                    email = currentUser?.email.toString(),
                    displayName = displayNameInput,
                    avatarUrl = "",
                    fcmToken = ""
                )

                profileViewModel.createUserProfile(userMock)
            }) {
                Text("Update Profile")
            }
        }


    }
}