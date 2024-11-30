package com.okariastudio.undevezhtriger.data.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message reçu: ${remoteMessage.data}")
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Nouveau token: $token")
    }
}
