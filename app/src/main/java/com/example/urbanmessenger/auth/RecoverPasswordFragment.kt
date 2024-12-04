package com.example.urbanmessenger.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.databinding.FragmentRecoverPasswordBinding
import com.example.urbanmessenger.utils.myToast

class RecoverPasswordFragment : Fragment() {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)

        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}