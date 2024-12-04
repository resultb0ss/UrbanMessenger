package com.example.urbanmessenger.menu_fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.updatePhone
import com.example.urbanmessenger.data.network.updatePhotoUri
import com.example.urbanmessenger.databinding.FragmentChangePhotoBinding
import com.example.urbanmessenger.utils.myToast
import com.google.android.material.button.MaterialButton
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat.checkSelfPermission


class ChangePhotoFragment : Fragment() {

    private var _binding: FragmentChangePhotoBinding? = null
    private val binding get() = _binding!!
    val GALLERY_REQUEST = 302
    val PHOTO_REQUEST_CODE = 301
    val PHOTO_PERMISSIONS_REQUEST_CODE = 303
    var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initButtons()
    }

    private fun initButtons() {
        binding.changePhotoFragmentChoosePhotoButton.setOnClickListener { getCustomAlertDialog() }
        binding.changePhotoFragmentSavePhotoButton.setOnClickListener{setPhoto()}
    }

    private fun setPhoto(){
        updatePhotoUri(photoUri.toString())
        findNavController().navigate(R.id.profileFragment)
        myToast("Фото успешно обновлено")

    }

    private fun getCustomAlertDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val view = layoutInflater.inflate(R.layout.alert_dialog_change_photo, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val alertDialog = builder.create()
        val buttonGallery =
            view.findViewById<MaterialButton>(R.id.changePhotoFragmentAlertDialogGalleryButton)
        val buttonCamera =
            view.findViewById<MaterialButton>(R.id.changePhotoFragmentAlertDialogCameraButton)

        builder.apply {
            setTitle("Выбрать/Сделать фото из")
            setCancelable(false)
            setNegativeButton("Отмена") { _, _ -> }
            buttonGallery.setOnClickListener{
                getImageFromGallery()
                alertDialog.cancel()
            }
            buttonCamera.setOnClickListener{
//                getImageFromCamera()
                alertDialog.cancel()}
        }
        alertDialog.show()
    }

//    private fun getImageFromCamera(){
//
//        when {
//            checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
//                startActivityForResult(
//                    Intent(MediaStore.ACTION_IMAGE_CAPTURE),
//                    PHOTO_REQUEST_CODE
//                )
//            }
//            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
//            }
//            else -> {
//                requestPermissions(
//                    arrayOf(Manifest.permission.CAMERA),
//                    PHOTO_PERMISSIONS_REQUEST_CODE
//                )
//            }
//        }
//    }

    private fun getImageFromGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent,GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GALLERY_REQUEST -> if(resultCode == RESULT_OK){
                photoUri = data?.data
                binding.changePhotoFragmentImage.setImageURI(photoUri)
            }
            PHOTO_REQUEST_CODE -> if(resultCode == RESULT_OK){
                photoUri = data?.data
                Log.d("@@@","PhotoUri $photoUri")
                binding.changePhotoFragmentImage.setImageURI(photoUri)
            }

        }
    }


}