package com.example.check

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check.ui.theme.CheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    InternetStatusApp(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun InternetStatusApp(modifier: Modifier = Modifier) {
    var isConnected by remember { mutableStateOf(false) }
    val connectivityManager = LocalContext.current.getSystemService(ConnectivityManager::class.java)

    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
            }

            override fun onLost(network: Network) {
                isConnected = false
            }
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager?.registerNetworkCallback(request, networkCallback)

        onDispose {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isConnected) Color.Green else Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isConnected) "Подключено к интернету" else "Интернет недоступен",
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}
