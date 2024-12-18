package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.sendMessageToSupportInFirebase
import com.example.urbanmessenger.databinding.FragmentSupportBinding
import com.example.urbanmessenger.utilits.myToast


class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.supportFragmentSendMessageButton.setOnClickListener{
            sendMessageToSupport()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendMessageToSupport(){
        val message = binding.supportFragmentMessageEditText.editText?.text.toString()
        if (message.isEmpty()){
            myToast("Введите пожалуйста сообщение")
        } else {
            sendMessageToSupportInFirebase(message) {
                myToast("Сообщение успешно отправлено, ответ придет на email ${USER.email}")
                binding.supportFragmentMessageEditText.editText?.text?.clear()
            }
        }
    }
}