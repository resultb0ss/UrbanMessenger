package com.example.urbanmessenger.menu_fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.updatePhone
import com.example.urbanmessenger.databinding.FragmentChangePhotoBinding
import com.example.urbanmessenger.utils.myToast
import com.google.android.material.button.MaterialButton


class ChangePhotoFragment : Fragment() {

    private var _binding: FragmentChangePhotoBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun getCustomAlertDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val view = layoutInflater.inflate(R.layout.alert_dialog_change_photo, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val buttonGallery =
            view.findViewById<MaterialButton>(R.id.changePhotoFragmentAlertDialogGalleryButton)
        val buttonCamera =
            view.findViewById<MaterialButton>(R.id.changePhotoFragmentAlertDialogCameraButton)

        builder.apply {
            setTitle("Выбрать/Сделать фото из")
            setCancelable(false)
            setNegativeButton("Отмена") { _, _ -> }
            buttonGallery.setOnClickListener{myToast("Нажали на галерею")}
            buttonCamera.setOnClickListener{myToast("Нажали на камеру")}
        }
        builder.create().show()
    }


//
//    private fun takePhoto() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivityForResult(intent, 0)
//        }
//    }
//
//    fun selectImageInAlbum() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivityForResult(intent, 1)
//        }
//    }
}