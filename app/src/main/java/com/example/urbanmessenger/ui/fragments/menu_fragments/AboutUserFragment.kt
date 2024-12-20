package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.urbanmessenger.databinding.FragmentAboutUserBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.CONTACT
import com.example.urbanmessenger.utilits.myToast


class AboutUserFragment : BaseFragment<FragmentAboutUserBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutUserBinding {
        return FragmentAboutUserBinding.inflate(inflater, container, false)
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
        binding.aboutUserFragmentContactCallButton.setOnClickListener { callToContact(CONTACT.phone) }
        binding.aboutUserFragmentContactSendMessageButton.setOnClickListener {
            sendSmsToContact(
                CONTACT.phone
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