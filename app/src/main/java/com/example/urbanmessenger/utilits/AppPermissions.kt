package com.example.urbanmessenger.utilits

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat


val getPermission =
    APP_ACTIVITY.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            myToast("Разрешение дано")

        } else {
            myToast("Разрешение не дано")
        }
    }

fun checkMyPermissions(permission: String, function: () -> Unit) {
    if (ActivityCompat.checkSelfPermission(
            APP_ACTIVITY.applicationContext,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        function()
    }
}

fun checkMyPermissions(permission: String, onSuccess: () -> Unit, onFailure:() -> Unit) {
    if (ActivityCompat.checkSelfPermission(
            APP_ACTIVITY.applicationContext,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onFailure()
    } else {
        onSuccess()
    }
}

