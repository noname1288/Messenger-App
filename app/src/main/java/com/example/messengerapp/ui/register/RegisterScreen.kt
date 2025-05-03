package com.example.messengerapp.ui.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messengerapp.navigation.AppRoute
import com.example.messengerapp.ui.login.AuthState
import com.example.messengerapp.ui.login.AuthViewModel
import com.example.messengerapp.ui.login.LoadingDialog

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate(AppRoute.HOME)
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            is AuthState.Loading -> showDialog = true
            else -> Unit
        }
    }

    if (showDialog){
        LoadingDialog()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(85.dp))
        Text(
            "Sign up", style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Welcome to messenger app",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.height(85.dp))
        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisibility) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisibility) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) { Icon(icon, contentDescription = "icon password visibility") }
            }
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val errorMessage = authViewModel.validateCredentials(emailInput, passwordInput)
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            } else { authViewModel.signup(emailInput, passwordInput) }
        }) {
            Text("Register")
        }
        Spacer(Modifier.height(8.dp))
        Text("Already have an account? Login", modifier = Modifier.clickable {
            navController.popBackStack(AppRoute.LOGIN, inclusive = false)
        })
    }
}
