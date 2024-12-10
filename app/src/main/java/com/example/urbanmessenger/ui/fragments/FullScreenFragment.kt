package com.example.urbanmessenger.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.urbanmessenger.R

class FullScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mDialog = Dialog(requireContext(),R.style.AppBaseTheme)
        mDialog.setContentView(R.layout.fragment_full_screen)
        mDialog.show()
    }


}