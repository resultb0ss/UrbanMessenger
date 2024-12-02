package com.example.urbanmessenger.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.urbanmessenger.APP_ACTIVITY

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val PERMISSION_REQUEST = 200

fun checkPermission(permission: String): Boolean {
    return if (true && ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)

        false
    } else true
}


//private val requestPermissionLauncher = registerForActivityResult(
//    ActivityResultContracts.RequestPermission(),
//) { isGranted: Boolean ->
//    if (isGranted) {
//        // FCM SDK (and your app) can post notifications.
//    } else {
//        // TODO: Inform user that that your app will not show notifications.
//    }
//}
//
//private fun askNotificationPermission() {
//    // This is only necessary for API level >= 33 (TIRAMISU)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
//            PackageManager.PERMISSION_GRANTED
//        ) {
//            // FCM SDK (and your app) can post notifications.
//        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//            // TODO: display an educational UI explaining to the user the features that will be enabled
//            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//            //       If the user selects "No thanks," allow the user to continue without notifications.
//        } else {
//            // Directly ask for the permission
//            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//        }
//    }
//}
