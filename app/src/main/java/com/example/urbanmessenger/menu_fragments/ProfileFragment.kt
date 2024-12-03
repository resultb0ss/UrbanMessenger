package com.example.urbanmessenger.menu_fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.initUser
import com.example.urbanmessenger.data.network.updatePhone
import com.example.urbanmessenger.data.network.updateProfileInfo
import com.example.urbanmessenger.databinding.FragmentProfileBinding
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

        binding.profileFragmentSaveInfoButton.setOnClickListener { saveProfileInfo() }
        binding.profileFragmentAddPhoneNumberButton.setOnClickListener { getCustomAlertDialog() }

    }

    @SuppressLint("InflateParams")
    private fun getCustomAlertDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val view = layoutInflater.inflate(R.layout.alert_dialog_with_edit_text, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val newPhone = view.findViewById<EditText>(R.id.alertDialogEditText)

        builder.apply {
            setTitle("Введите номер телефона")
            setCancelable(false)
            setPositiveButton("Добавить номер") { _, _ ->
                updatePhone(newPhone.text.toString())
            }
            setNegativeButton("Отмена") { _, _ -> }
        }
        builder.create().show()
    }

    private fun saveProfileInfo() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


