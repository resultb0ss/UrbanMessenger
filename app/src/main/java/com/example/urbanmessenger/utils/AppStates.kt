package com.example.urbanmessenger.utils

import android.content.Context
import com.example.urbanmessenger.AUTHFIREBASE
import com.example.urbanmessenger.CHILD_STATE
import com.example.urbanmessenger.DATA_BASE_ROOT
import com.example.urbanmessenger.NODE_USERS
import com.example.urbanmessenger.UID
import com.example.urbanmessenger.USER

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updateState(appStates: AppStates, context:Context) {

            if (AUTHFIREBASE.currentUser!= null){
                DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener{ USER.state = appStates.state }
                    .addOnFailureListener{myToast("Не удалось обновить статус", context)}
            }

        }
    }
}