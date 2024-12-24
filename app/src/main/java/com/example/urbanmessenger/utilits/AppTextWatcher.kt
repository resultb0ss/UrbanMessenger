package com.example.urbanmessenger.utilits


import android.text.Editable
import android.text.TextWatcher

open class AppTextWatcher(val function: (p0: CharSequence?) -> Unit): TextWatcher {
    override fun beforeTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {

    }

    override fun onTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        function(p0)
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}