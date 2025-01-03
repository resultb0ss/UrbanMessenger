package com.example.urbanmessenger.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.data.network.CHILD_ID
import com.example.urbanmessenger.data.network.CHILD_MAIL
import com.example.urbanmessenger.data.network.CHILD_USERNAME
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.databinding.FragmentRegistrationBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.myToast
import com.google.firebase.auth.FirebaseAuth


class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
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
            myToast("Заполните все необходимые поля")
            return
        }
        if (password != confirmPassword) {
            myToast("Ваши пароли не совпадают")
            return
        }

        AUTHFIREBASE.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    var dataMap: MutableMap<String, Any> = mutableMapOf<String, Any>()
                    val uid = AUTHFIREBASE.currentUser?.uid.toString()

                    dataMap[CHILD_ID] = uid
                    dataMap[CHILD_MAIL] = email
                    dataMap[CHILD_USERNAME] = uid

                    DATA_BASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                myToast("Регистрация прошла успешно")
                                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                            } else {
                                myToast(it.exception?.message.toString())
                            }
                        }

                } else {
                    if (AUTHFIREBASE.currentUser != null) {
                        myToast("Пользователь с такой почтой уже существует")
                        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    }
                    myToast(
                        "Не удалось зарегистрироваться ${it.exception?.message.toString()}"
                    )
                }
            }
    }

}