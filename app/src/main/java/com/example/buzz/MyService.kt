package com.example.buzz

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MessagingCloud", "Token: $token")

    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("MessagingCloud", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MessagingCloud", "Message data payload: ${remoteMessage.data}")


        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MessagingCloud", "Message Notification Body: ${it.body}")
        }
    }
}