package com.example.messengerapp.ui

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.messengerapp.navigation.AppNavHost
import androidx.compose.runtime.getValue

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()


    //Theo doi route hien tai
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(currentRoute){ Log.d("Navigation", "currentRoute: $currentRoute") }

    Scaffold (
    ){
        innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }



}