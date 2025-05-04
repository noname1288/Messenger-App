package com.example.messengerapp.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messengerapp.core.UIState
import com.example.messengerapp.navigation.AppRoute
import com.example.messengerapp.ui.AuthViewModel

@Composable
fun LoginScreen( navController: NavController) {
    val context = LocalContext.current

    //declare viewmodel
    //option 1: using popularly in jetpackcompose
    var authViewModel : AuthViewModel = viewModel()
    /*
    * BENEFIT:
    *Bound to lifecycle of activity
    *Preservers state during recomposition
    *Reuses the viewmodel instance
    * ******
    * */

//    option2:
//    val context = LocalContext.current as ComponentActivity
//    var authViewModel2 = ViewModelProvider(context)[AuthViewModel::.java]

    var emailInput by remember{mutableStateOf("")}
    var passwordInput by remember{mutableStateOf("")}
    var passwordVisibility by remember{mutableStateOf(false)}
    var showDialog by remember { mutableStateOf(false) }


    val authState = authViewModel.signInState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is UIState.Authenticated -> {
                showDialog = false
                navController.navigate(AppRoute.HOME)
            }
            is UIState.Error -> {
                showDialog = false

                Toast.makeText(
                    context,
                    (authState.value as UIState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()

                Log.e("LoginScreen", (authState.value as UIState.Error).message)
            }
            is UIState.Loading -> showDialog = true
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
                    VisualTransformation.None
                } else {

                    PasswordVisualTransformation()
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
            authViewModel.login(emailInput, passwordInput)
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
