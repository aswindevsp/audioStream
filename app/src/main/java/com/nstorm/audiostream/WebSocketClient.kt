package com.nstorm.audiostream

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.websocket.Frame
import io.ktor.websocket.readText

class WebSocketClient(private val url: String) {

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun sendMessage(message: String) {
        client.ws(url) {
            send(Frame.Text(message))
        }
    }

    suspend fun connect(listener: WebSocketListener) {
        client.ws(url) {
            listener.onConnected()
            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        listener.onMessage(frame.readText())
                    }
                }
            } catch (e: Exception) {
                listener.onDisconnected()
            }
        }
    }



    fun disconnect() {
        client.close()
    }
}