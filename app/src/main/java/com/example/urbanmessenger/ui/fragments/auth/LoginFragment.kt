package com.example.urbanmessenger.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.MainActivity
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.databinding.FragmentLoginBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.myToast


class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginFragmentLoginButton.setOnClickListener {
            login()
        }

        binding.loginFragmentRegistrationTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.loginFragmentRecoverPasswordText.setOnClickListener {
            findNavController().navigate(R.id.recoverPasswordFragment)
        }
    }

    private fun login() {
        val email = binding.loginFragmentEmailField.editText?.text.toString()
        val password = binding.loginFragmentPasswordField.editText?.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AUTHFIREBASE.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        myToast("Успешный вход в систему")
                        startActivity(Intent(requireActivity(), MainActivity::class.java))

                    } else {
                        myToast("Не удалось войти в систему")
                    }
                }
        } else {
            myToast("Заполните необходимые поля")
        }
    }

}