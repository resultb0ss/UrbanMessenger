package com.example.urbanmessenger

import android.content.Context
import android.widget.Toast

fun myToast(text: String, context: Context) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}