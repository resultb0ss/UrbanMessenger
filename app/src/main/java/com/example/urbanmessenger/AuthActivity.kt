package com.example.urbanmessenger

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.data.network.initFirebase
import com.example.urbanmessenger.databinding.ActivityAuthBinding
import com.example.urbanmessenger.utilits.myToast

class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        initStoragePermission()

    }

    private fun initStoragePermission() {

        var permission = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES
        } else permission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (AUTHFIREBASE.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
            myToast("Чтобы продолжить дальше, необходимо разрешение!")
        } else {
            permissionLauncherSingle.launch(permission)
        }
    }

    private val permissionLauncherSingle = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            myToast("Разрешение на получение фото получено")
        } else {
            myToast("Разрешение на получение фото не получено")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
