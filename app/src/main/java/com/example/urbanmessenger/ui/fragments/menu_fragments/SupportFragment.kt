package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.sendMessageToSupportInFirebase
import com.example.urbanmessenger.databinding.FragmentSupportBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.myToast


class SupportFragment : BaseFragment<FragmentSupportBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSupportBinding {
        return FragmentSupportBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.supportFragmentSendMessageButton.setOnClickListener {
            sendMessageToSupport()
        }
    }

    private fun sendMessageToSupport() {
        val message = binding.supportFragmentMessageEditText.editText?.text.toString()
        if (message.isEmpty()) {
            myToast("Введите пожалуйста сообщение")
        } else {
            sendMessageToSupportInFirebase(message) {
                myToast("Сообщение успешно отправлено, ответ придет на email ${USER.email}")
                binding.supportFragmentMessageEditText.editText?.text?.clear()
            }
        }
    }
}