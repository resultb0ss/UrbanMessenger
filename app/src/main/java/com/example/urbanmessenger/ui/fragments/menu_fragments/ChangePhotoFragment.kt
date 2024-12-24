package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.SupabaseClient.supabaseClient
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.updatePhotoUri
import com.example.urbanmessenger.data.network.uploadPhotoToSupabaseStorage
import com.example.urbanmessenger.databinding.FragmentChangePhotoBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.myToast
import com.example.urbanmessenger.utilits.uriToByteArray
import com.google.android.material.button.MaterialButton
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import kotlin.time.Duration.Companion.minutes


class ChangePhotoFragment : BaseFragment<FragmentChangePhotoBinding>() {

    var photoUri: Uri? = null


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangePhotoBinding {
        return FragmentChangePhotoBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        initButtons()
    }


    private fun initButtons() {
        binding.changePhotoFragmentChoosePhotoButton.setOnClickListener { getCustomAlertDialog() }
        binding.changePhotoFragmentSavePhotoButton.setOnClickListener { setPhoto() }
    }


    private fun setPhoto() {
        if (photoUri != null) {
//            val imageByteArray = photoUri?.uriToByteArray(requireContext())
//            val photoName =
//                "${USER.id}/${System.currentTimeMillis()}/${photoUri.toString().split("/").last()}"
//            lifecycleScope.launch {
//                imageByteArray?.let {
//                    uploadFile("main", photoName, it)
//                }
//            }

            uploadPhotoToSupabaseStorage(photoUri,"main"){
                uri -> updatePhotoUri(uri)
            }

            findNavController().navigate(R.id.profileFragment)
            myToast("Фото успешно обновлено")
        } else {
            myToast("Вы ничего не выбрали")
        }


    }

    //НЕ УДАЛЯЙ!!!! ПРОВЕРЬ НА РАЗНЫХ ФОТО
//    private fun uploadFile(bucketName: String, fileName: String, byteArray: ByteArray) {
//        lifecycleScope.launch {
//            val bucket = supabaseClient.storage[bucketName]
//            bucket.upload("$fileName.jpg", byteArray) {
//                upsert = false
//            }
//            readFile("main", fileName)
//        }
//    }
//
//
//    private fun readFile(
//        bucketName: String,
//        fileName: String,
//    ) {
//        lifecycleScope.launch {
//            try {
//                val bucket = supabaseClient.storage.from(bucketName)
//                val url = bucket.createSignedUrl("$fileName.jpg", expiresIn = 20.minutes)
//                updatePhotoUri(url)
//            } catch (e: Exception) {
//                myToast(e.message.toString())
//            }
//        }
//    }

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
            buttonGallery.setOnClickListener {
                pickFromGallery()
                alertDialog.cancel()

            }
            buttonCamera.setOnClickListener {
                pickFromCamera()
                alertDialog.cancel()
            }
        }
        alertDialog.show()
    }

    val permissionLauncherSingle =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                myToast("Разрешение дано")
            } else {
                myToast("Разрешение не дано")
            }
        }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            photoUri = uri
            binding.changePhotoFragmentImage.setImageURI(photoUri)
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            photoUri = getImageUriFromBitmap(bitmap)
            binding.changePhotoFragmentImage.setImageBitmap(bitmap)
        }

    private fun pickFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            galleryLauncher.launch("image/*")
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            myToast("Оно необходимо чтобы сделать фото")
        } else {
            permissionLauncherSingle.launch(Manifest.permission.CAMERA)
        }

    }

    private fun pickFromCamera() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            myToast("Оно необходимо чтобы сделать фото")
        } else {
            permissionLauncherSingle.launch(Manifest.permission.CAMERA)
        }
    }


    private fun getImageUriFromBitmap(bitmap: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path.toString())
    }


}