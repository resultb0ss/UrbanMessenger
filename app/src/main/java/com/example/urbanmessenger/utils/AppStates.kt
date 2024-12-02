package com.example.urbanmessenger.utils

import android.content.Context
import com.example.urbanmessenger.database.AUTHFIREBASE
import com.example.urbanmessenger.database.CHILD_STATE
import com.example.urbanmessenger.database.DATA_BASE_ROOT
import com.example.urbanmessenger.database.NODE_USERS
import com.example.urbanmessenger.database.UID
import com.example.urbanmessenger.database.USER

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updateState(appStates: AppStates, context: Context) {

            if (AUTHFIREBASE.currentUser != null) {
                DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener { USER.state = appStates.state }
                    .addOnFailureListener { myToast("Не удалось обновить статус") }
            }

        }
    }
}