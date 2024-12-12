package com.example.urbanmessenger.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.urbanmessenger.databinding.FragmentFullScreenBinding
import com.example.urbanmessenger.models.UserData

class FullScreenFragment(var message: UserData) : DialogFragment() {

    lateinit var binding: FragmentFullScreenBinding

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = FragmentFullScreenBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        binding.singleChatFullScreenImageView.setImageURI(message.imageUriSender.toUri())
        builder.setView(binding.root)

        return builder
            .setTitle("")
            .setPositiveButton("Ok") { _, _ -> }
            .create()

    }

}