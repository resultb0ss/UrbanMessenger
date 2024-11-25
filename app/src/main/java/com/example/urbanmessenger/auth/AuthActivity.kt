package com.example.urbanmessenger.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.urbanmessenger.AUTHFIREBASE
import com.example.urbanmessenger.chats.MainActivity
import com.example.urbanmessenger.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AUTHFIREBASE = FirebaseAuth.getInstance()

        if (AUTHFIREBASE.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}