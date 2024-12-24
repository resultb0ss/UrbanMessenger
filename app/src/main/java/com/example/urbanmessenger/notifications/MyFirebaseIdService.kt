package com.example.urbanmessenger.notifications

import android.util.Log
import com.example.urbanmessenger.data.network.FIREBASEMESSAGING
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.updateToken
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {

    private var _token: String? = null
    private val token get() = _token!!

    override fun onNewToken(s: String) {
        super.onNewToken(s)

        FIREBASEMESSAGING = FirebaseMessaging.getInstance()

        FIREBASEMESSAGING.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _token = task.result
                Log.d("@@@", "Token: ${token}")
            } else {
                Log.d("@@@", "Failed to get token")
            }
        }
        var refreshToken: String = token

        if (UID != null) {
            updateToken(refreshToken)
            Log.d("@@@", "Token refresh: ${refreshToken}")
        }
    }


}