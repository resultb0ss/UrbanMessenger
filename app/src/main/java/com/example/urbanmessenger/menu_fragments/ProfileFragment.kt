package com.example.urbanmessenger.menu_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.database.USER
import com.example.urbanmessenger.databinding.FragmentProfileBinding
import com.example.urbanmessenger.database.initUser
import com.example.urbanmessenger.database.updateProfileInfo
import com.example.urbanmessenger.utils.myToast


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileFragmentSaveInfoButton.setOnClickListener {

            val newFirstName =
                binding.profileFragmentChangeFirstNameTextField.editText?.text.toString()
            val newLastName =
                binding.profileFragmentChangeLastNameTextField.editText?.text.toString()
            val newProfession =
                binding.profileFragmentChangeProfessionTextField.editText?.text.toString()
            val newAddress = binding.profileFragmentChangeAdressTextField.editText?.text.toString()
            val newAge = binding.profileFragmentChangeAgeTextField.editText?.text.toString()

            updateProfileInfo(newFirstName, newLastName, newProfession, newAge, newAddress) {
                myToast("Данные успешно обновлены")
            }

        }
    }


    override fun onResume() {
        super.onResume()
        initUser()
        initFields()
    }

    private fun initFields() {
        binding.profileFragmentUserNameTextView.text = USER.username
        binding.profileFragmentUserMailTextView.text = USER.email
        binding.profileFragmentChangeFirstNameTextField.editText?.setText(USER.firstname)
        binding.profileFragmentChangeLastNameTextField.editText?.setText(USER.lastname)
        binding.profileFragmentChangeAgeTextField.editText?.setText(USER.age)
        binding.profileFragmentChangeAdressTextField.editText?.setText(USER.address)
        binding.profileFragmentChangeProfessionTextField.editText?.setText(USER.profession)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


