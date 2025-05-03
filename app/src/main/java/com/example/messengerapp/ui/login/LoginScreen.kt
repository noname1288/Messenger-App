package com.example.messengerapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.messengerapp.navigation.AppRoute

@Composable
fun LoginScreen( navController: NavController, authViewModel: AuthViewModel) {
    var emailInput by remember{mutableStateOf("")}
    var passwordInput by remember{mutableStateOf("")}
    var passwordVisibility by remember{mutableStateOf(false)}
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

        Text(text = "Sign In", fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Welcome to messenger app",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )

        Spacer(Modifier.height(85.dp))

        //Input Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp)
        ) {
            /*Email input*/
            OutlinedTextField(
                value = emailInput,
                onValueChange = {emailInput = it},
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            /*Password input*/
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password") },
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
                    }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(24.dp))
        }

        //button area
        Button(onClick = {
            val errorMessage = authViewModel.validateCredentials(emailInput, passwordInput)
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            } else { authViewModel.login(emailInput, passwordInput) }
        }) {
            Text(text = "Login")
        }

        Spacer(Modifier.height(16.dp))

        //Register
        Text(text = "Register if you don't have an account", modifier = Modifier.clickable {
            navController.navigate(AppRoute.REGISTER)
        })

    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Box (modifier = Modifier
            .size(100.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
}
