package com.example.urbanmessenger.ui.fragments.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.databinding.FragmentRecoverPasswordBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.myToast

class RecoverPasswordFragment : BaseFragment<FragmentRecoverPasswordBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecoverPasswordBinding {
        return FragmentRecoverPasswordBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        initFields()
    }

    private fun initFields() {
        binding.recoverFragmentSendEmailButton.setOnClickListener { recoverPassword() }
    }

    private fun recoverPassword() {
        val mail = binding.recoverFragmentEmailField.text.toString()
        if (mail.isEmpty()) {
            myToast("Заполните поле электронной почты")
        } else {
            AUTHFIREBASE.sendPasswordResetEmail(mail).addOnSuccessListener {
                myToast("Ссылка для восстановления пароля направлена на почту $mail")
                findNavController().navigate(R.id.action_recoverPasswordFragment_to_loginFragment)
            }
                .addOnFailureListener { it.message?.toString() }
        }

    }


}