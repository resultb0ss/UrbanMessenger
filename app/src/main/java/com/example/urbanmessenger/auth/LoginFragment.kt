package com.example.urbanmessenger.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.AUTHFIREBASE
import com.example.urbanmessenger.R
import com.example.urbanmessenger.chats.MainActivity
import com.example.urbanmessenger.databinding.FragmentLoginBinding
import com.example.urbanmessenger.myToast


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
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
        val email = binding.loginFragmentEmailField.text.toString()
        val password = binding.loginFragmentPasswordField.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AUTHFIREBASE.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        myToast("Успешный вход в систему", requireContext())
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                    } else {
                        myToast("Не удалось войти в систему", requireContext())
                    }
                }
        } else {
            myToast("Заполните необходимые поля", requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}