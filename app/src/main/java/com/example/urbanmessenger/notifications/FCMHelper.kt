package com.example.urbanmessenger.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

lateinit var FIREBASEMESSAGING: FirebaseMessaging

fun initFirebaseCloudMessaging(){
    FIREBASEMESSAGING = FirebaseMessaging.getInstance()

    FIREBASEMESSAGING.token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            Log.d("@@@","Token: $token")
        } else {
            Log.d("@@@","Failed to get token")
        }
    }
}