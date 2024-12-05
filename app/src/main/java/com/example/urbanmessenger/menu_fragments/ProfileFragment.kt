package com.example.urbanmessenger.menu_fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.CONTACT
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.data.network.initUser
import com.example.urbanmessenger.data.network.updatePhone
import com.example.urbanmessenger.data.network.updateProfileInfo
import com.example.urbanmessenger.databinding.FragmentProfileBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppValueEventListener
import com.example.urbanmessenger.utils.myToast
import com.google.firebase.database.DatabaseReference


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mListenerMainInfoBlock: AppValueEventListener
    private lateinit var mReceivingUser: UserData
    private lateinit var mRefUser: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        initUser()
        initFields()
        setValueEventListener()
    }

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(mListenerMainInfoBlock)
    }

    private fun setValueEventListener(){
        mListenerMainInfoBlock = AppValueEventListener {
            mReceivingUser = it.getUserDataModel()
            initMainInfoBlock()
        }

        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(UID)
        mRefUser.addValueEventListener(mListenerMainInfoBlock)
    }

    private fun initMainInfoBlock(){
        binding.profileFragmentUserImage.setImageURI(mReceivingUser.userPhotoUri.toUri())
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
        binding.userImageBlock.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePhotoFragment)
        }

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

        val newFirstName = binding.profileFragmentChangeFirstNameTextField.editText?.text.toString()
        val newLastName = binding.profileFragmentChangeLastNameTextField.editText?.text.toString()
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



