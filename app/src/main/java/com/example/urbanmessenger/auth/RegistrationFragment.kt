package com.example.urbanmessenger.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.AUTHFIREBASE
import com.example.urbanmessenger.R
import com.example.urbanmessenger.databinding.FragmentRegistrationBinding
import com.example.urbanmessenger.myToast
import com.google.firebase.auth.FirebaseAuth


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AUTHFIREBASE = FirebaseAuth.getInstance()

        binding.signInButtonBTN.setOnClickListener {
            signUpUser()
        }

        binding.tvRedirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun signUpUser() {
        val email = binding.emailSignUpEditText.text.toString()
        val password = binding.passwordSignUpEditText.text.toString()
        val confirmPassword = binding.confirmPasswordSignUpEditText.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            myToast("Заполните все необходимые поля", requireContext())
            return
        }
        if (password != confirmPassword) {
            myToast("Ваши пароли не совпадают", requireContext())
            return
        }

        AUTHFIREBASE.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    myToast("Регистрация прошла успешно", requireContext())
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                } else {
                    if (AUTHFIREBASE.currentUser != null) {
                        myToast("Пользователь с такой почтой уже существует", requireContext())
                        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    }
                    myToast("Не удалось зарегистрироваться", requireContext())
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}