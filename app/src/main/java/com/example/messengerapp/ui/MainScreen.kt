package com.example.messengerapp.ui

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.messengerapp.core.UIState
import com.example.messengerapp.domain.session.SessionManager
import com.example.messengerapp.navigation.AppNavHost
import com.example.messengerapp.navigation.AppRoute
import com.example.messengerapp.navigation.navigateRoot
import com.example.messengerapp.navigation.navigateWithArgs
import com.example.messengerapp.service_locator.AppContainer
import com.example.messengerapp.ui.login.AuthViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val authViewModel : AuthViewModel = viewModel()
    val authState  = authViewModel.signInState.observeAsState()

    //XIN QUYEN THONG BAO
    val context = LocalContext.current
//    val activity = context as? ComponentActivity

    // Cờ nhận biết mở từ thông báo
    val startedFromNotification = remember { mutableStateOf(false) }

    //launcher xin quyền
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            Toast.makeText(context, "Đã cấp quyền thông báo", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Chưa cấp quyền thông báo", Toast.LENGTH_SHORT).show()
    }

    // chỉ gọi 1 lần khi app chạy
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Điều hướng vào chat nếu được mở từ notification
    val activity = context as? ComponentActivity
    LaunchedEffect(Unit) {
        val chatId = activity?.intent?.getStringExtra("chatId")
        if (!chatId.isNullOrBlank()) {
            Log.d("FCM", "Navigating to CHAT/$chatId from notification")

            startedFromNotification.value = true

            navController.navigateWithArgs("CHAT/%s", chatId)

            // Xoá chatId để không điều hướng lại lần sau
            activity.intent?.removeExtra("chatId")
        }
    }

    //Nếu vừa login thành công và KHÔNG mở từ notification → vào HOME
    LaunchedEffect(authState.value) {
        if (authState.value is UIState.Authenticated && !startedFromNotification.value) {
            val uid = AppContainer.firebaseAuth.currentUser?.uid.orEmpty()
            val result = SessionManager.fetch(uid, AppContainer.userRepository)
            if (result.isSuccess) {
                navController.navigateRoot(AppRoute.HOME)
            } else {
                Toast.makeText(context, "Không lấy được thông tin user", Toast.LENGTH_SHORT).show()
            }
        }
    }




    //Theo doi route hien tai
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(currentRoute) { Log.d("Navigation", "currentRoute: $currentRoute") }

    Scaffold{ innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }


}