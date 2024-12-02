package com.example.urbanmessenger.menu_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.CHILD_ADDRESS
import com.example.urbanmessenger.CHILD_AGE
import com.example.urbanmessenger.CHILD_FIRSTNAME
import com.example.urbanmessenger.CHILD_LASTNAME
import com.example.urbanmessenger.CHILD_PROFESSION
import com.example.urbanmessenger.DATA_BASE_ROOT
import com.example.urbanmessenger.NODE_USERS
import com.example.urbanmessenger.UID
import com.example.urbanmessenger.USER
import com.example.urbanmessenger.databinding.FragmentProfileBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppValueEventListener
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

            val newFirstName = binding.profileFragmentChangeFirstNameTextField.editText?.text.toString()
            val newLastName = binding.profileFragmentChangeLastNameTextField.editText?.text.toString()
            val newProfession = binding.profileFragmentChangeProfessionTextField.editText?.text.toString()
            val newAddress = binding.profileFragmentChangeAdressTextField.editText?.text.toString()
            val newAge = binding.profileFragmentChangeAgeTextField.editText?.text.toString()

            updateFirstName(newFirstName)
            updateLastName(newLastName)
            updateAge(newAge)
            updateAddress(newAddress)
            updateProfession(newProfession)

            myToast("Данные успешно обновлены", requireContext())
        }
    }


    private fun updateProfession(newProfession: String){
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PROFESSION).setValue(newProfession)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    USER.profession = newProfession
                } else {
                    myToast(it.exception?.message.toString(), requireContext())
                }
            }
    }

    private fun updateAddress(newAddress: String) {
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_ADDRESS).setValue(newAddress)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    USER.address = newAddress
                } else {
                    myToast(it.exception?.message.toString(), requireContext())
                }
            }
    }

    private fun updateAge(newAge: String) {
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_AGE).setValue(newAge)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    USER.age = newAge
                } else {
                    myToast(it.exception?.message.toString(), requireContext())
                }
            }
    }

    private fun updateLastName(newLastName: String) {
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_LASTNAME).setValue(newLastName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    USER.lastname = newLastName
                } else {
                    myToast(it.exception?.message.toString(), requireContext())
                }
            }
    }

    private fun updateFirstName(newFirstName: String) {
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FIRSTNAME).setValue(newFirstName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    USER.firstname = newFirstName
                } else {
                    myToast(it.exception?.message.toString(), requireContext())
                }
            }
    }

    override fun onResume() {
        super.onResume()
        initUser()
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

    private fun initUser() {
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener {
                USER = it.getValue(UserData::class.java) ?: UserData()
            }
        )
        initFields()
    }


}