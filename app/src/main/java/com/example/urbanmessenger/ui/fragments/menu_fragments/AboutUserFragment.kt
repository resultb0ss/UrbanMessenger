package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.urbanmessenger.databinding.FragmentAboutUserBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.myToast


class AboutUserFragment : BaseFragment<FragmentAboutUserBinding>() {

    private lateinit var user: UserData

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutUserBinding {
        return FragmentAboutUserBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = AboutUserFragmentArgs.Companion.fromBundle(requireArguments()).user
    }

    override fun onResume() {
        super.onResume()
        initUsersInfoFields()

    }

    private fun initUsersInfoFields() {
        binding.aboutUserFragmentProfileHeaderEmail.text = user.email
        binding.aboutUserFragmentProfileHeaderID.text = user.id
        binding.aboutUserFragmentNameUserValue.text = user.firstname
        binding.aboutUserFragmentLastNameUserValue.text = user.lastname
        binding.aboutUserFragmentAgeUserValue.text = user.age
        binding.aboutUserFragmentAddressUserValue.text = user.address
        binding.aboutUserFragmentPhoneUserValue.text = user.phone
        binding.aboutUserFragmentProfessionUserValue.text = user.profession
        binding.aboutUserFragmentContactCallButton.setOnClickListener { callToContact(user.phone) }
        binding.aboutUserFragmentContactSendMessageButton.setOnClickListener {
            sendSmsToContact(
                user.phone
            )
        }

    }


    private fun callToContact(phoneNumber: String) {
        if (phoneNumber.isEmpty()) {
            myToast("Пользователь не добавил номер телефона, вы не можете ему позвонить")
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val phoneIntent = Intent(Intent.ACTION_CALL)
                phoneIntent.data = Uri.parse("tel:$phoneNumber")
                startActivity(phoneIntent)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                myToast("Оно необходимо для звонков")
            } else {
                permissionLauncherSingle.launch(Manifest.permission.CALL_PHONE)
            }
        }

    }

    private fun sendSmsToContact(phoneNumber: String) {
        if (phoneNumber.isEmpty()) {
            myToast("Пользователь не добавил номер телефона, вы не можете отпраить ему смс")
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val intentSms = Intent(Intent.ACTION_SENDTO)
                intentSms.data = Uri.parse("smsto:$phoneNumber")
                startActivity(intentSms)

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                myToast("Оно необходимо для отправки сообщений")
            } else {
                permissionLauncherSingle.launch(Manifest.permission.SEND_SMS)
            }
        }
    }

    private val permissionLauncherSingle = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            myToast("Разрешение получено")
        } else {
            myToast("Разрешение не получено")
        }

    }

}