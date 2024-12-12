package com.example.urbanmessenger.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

lateinit var FIREBASEMESSAGING: FirebaseMessaging

private var _token: String? = null
private val token get() = _token!!

fun initFirebaseCloudMessaging() {
    FIREBASEMESSAGING = FirebaseMessaging.getInstance()

    FIREBASEMESSAGING.token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            _token = task.result
            Log.d("@@@", "Token: $token")
        } else {
            Log.d("@@@", "Failed to get token")
        }
    }


}

fun getNewToken(): String {
    return token
}