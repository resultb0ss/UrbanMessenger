package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.utilits.CONTACT
import com.example.urbanmessenger.databinding.FragmentAboutUserBinding

class AboutUserFragment : Fragment() {

    private var _binding: FragmentAboutUserBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initUsersInfoFields()

    }

    private fun initUsersInfoFields() {
        binding.aboutUserFragmentProfileHeaderEmail.text = CONTACT.email
        binding.aboutUserFragmentProfileHeaderID.text = CONTACT.id
        binding.aboutUserFragmentNameUserValue.text = CONTACT.firstname
        binding.aboutUserFragmentLastNameUserValue.text = CONTACT.lastname
        binding.aboutUserFragmentAgeUserValue.text = CONTACT.age
        binding.aboutUserFragmentAddressUserValue.text = CONTACT.address
        binding.aboutUserFragmentPhoneUserValue.text = CONTACT.phone
        binding.aboutUserFragmentProfessionUserValue.text = CONTACT.profession
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}