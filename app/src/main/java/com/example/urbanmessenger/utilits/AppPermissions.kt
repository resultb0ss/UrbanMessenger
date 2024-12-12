package com.example.urbanmessenger.utilits

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat


val getPermission = APP_ACTIVITY.registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        myToast("Разрешение дано")

    } else {
        myToast("Разрешение не дано")
    }
}

fun askNotificationsPermission(permission: String, message: String, function: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY.applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            function()
        } else if (shouldShowRequestPermissionRationale(APP_ACTIVITY, permission)) {
            alertDialogForPermission(permission, message)
        } else {
            permissionLauncherSingle.launch(permission)
        }
    }
}

private val permissionLauncherSingle = APP_ACTIVITY.registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
    if (isGranted) {
        myToast("Разрешение получено")
    } else {
        myToast("Разрешение не получено")
    }

}

private fun alertDialogForPermission(permission: String, message: String) {
    val builder = AlertDialog.Builder(APP_ACTIVITY.applicationContext)
    builder.apply {
        setTitle("Разрешение необходимо, чтобы вы могли $message")
        setPositiveButton("Дать разрешение") { _, _ ->
            permissionLauncherSingle.launch(permission)
        }
        setNegativeButton("Не давать разрешение") { _, _ -> }
        create()
    }
    builder.show()
}



