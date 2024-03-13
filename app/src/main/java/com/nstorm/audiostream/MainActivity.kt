package com.nstorm.audiostream

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nstorm.audiostream.ui.theme.AudioStreamTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), WebSocketListener {


    private val webSocketClient = WebSocketClient("ws://192.168.1.37:8765")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        )

        setContent {


            val scope = rememberCoroutineScope()
            AudioStreamTheme{
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            scope.launch(Dispatchers.IO) {
                                webSocketClient.connect(this@MainActivity)
                            }
                        }) {
                            Text(text = "Connect")
                        }


                        Button(onClick = {
                            scope.launch(Dispatchers.IO) {
                                webSocketClient.sendMessage("Hello World")
                            }
                        }) {
                            Text(text = "Send Message")
                        }

                        Button(onClick = {
                            scope.launch(Dispatchers.IO) {
                                webSocketClient.disconnect()
                            }
                        }) {
                            Text(text = "Disconnect")
                        }
                    }
                }
            }
        }
    }

    override fun onConnected() {
        Log.d("MainActivity", "Connected")
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessage(message: String) {
        Log.d("MainActivity", "Message: $message")
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Message: $message", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDisconnected() {
        Log.d("MainActivity", "Disconnected")
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()
        }
    }
}
