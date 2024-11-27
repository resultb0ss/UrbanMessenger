package com.example.urbanmessenger.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.urbanmessenger.chats.MainActivity

fun myToast(text: String, context: Context) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun updateToolbar(title: String, activity: Activity) {
    (activity as MainActivity).updateToolbarTitle(title)
}