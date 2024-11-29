package com.example.urbanmessenger

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.urbanmessenger.chats.MainActivity
import com.example.urbanmessenger.databinding.FragmentSingleChatBinding

class SingleChatFragment : Fragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!
//    private lateinit var chatName: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
//        chatName = SingleChatFragmentArgs.Companion.fromBundle(requireArguments()).chatName
    }
    override fun onResume() {
        super.onResume()
//        APP_ACTIVITY.updateToolbarTitle(chatName)


    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}