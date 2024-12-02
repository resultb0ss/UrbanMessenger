package com.example.urbanmessenger.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.urbanmessenger.APP_ACTIVITY
import com.example.urbanmessenger.MainActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun myToast(text: String) {
    Toast.makeText(
        APP_ACTIVITY,
        text,
        Toast.LENGTH_SHORT
    ).show()
}


fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}