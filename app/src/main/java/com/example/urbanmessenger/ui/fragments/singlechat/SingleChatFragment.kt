package com.example.urbanmessenger.ui.fragments.singlechat

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_MESSAGES
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.TYPE_IMAGE
import com.example.urbanmessenger.data.network.TYPE_TEXT
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.data.network.removeMessage
import com.example.urbanmessenger.data.network.saveToMainList
import com.example.urbanmessenger.data.network.sendImageMessage
import com.example.urbanmessenger.data.network.sendMessage
import com.example.urbanmessenger.databinding.FragmentSingleChatBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.ui.fragments.FullScreenFragment
import com.example.urbanmessenger.utilits.APP_ACTIVITY
import com.example.urbanmessenger.utilits.AppChildEventListener
import com.example.urbanmessenger.utilits.AppTextWatcher
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.TYPE_CHAT
import com.example.urbanmessenger.utilits.myToast
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DatabaseReference

class SingleChatFragment : BaseFragment<FragmentSingleChatBinding>() {

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 15
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var user: UserData

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserData
    private lateinit var mRefUser: DatabaseReference

    private lateinit var toolbar: MaterialToolbar

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSingleChatBinding {
        return FragmentSingleChatBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = SingleChatFragmentArgs.Companion.fromBundle(requireArguments()).user
    }

    override fun onResume() {
        super.onResume()

        initFields()
        initRecyclerView()
        initToolbarWithListener()
    }

    private fun initToolbarWithListener() {
        toolbar = APP_ACTIVITY.findViewById<MaterialToolbar>(R.id.mainActivityToolbar)
        toolbar.visibility = View.GONE
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserDataModel()
            initInfoToolbar()
        }

        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(user.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
    }

    private fun initFields() {
        mLayoutManager = LinearLayoutManager(this.context)

        binding.messageInputField.addTextChangedListener(AppTextWatcher {
            val string = binding.messageInputField.text.toString()
            if (string.isEmpty()) {
                binding.singleChatFragmentSendMessageIcon.visibility = View.GONE
                binding.singleChatFragmentClipIcon.visibility = View.VISIBLE
            } else {
                binding.singleChatFragmentSendMessageIcon.visibility = View.VISIBLE
                binding.singleChatFragmentClipIcon.visibility = View.GONE
            }
        })

        binding.singleChatFragmentSendMessageIcon.setOnClickListener {
            mSmoothScrollToPosition = true

            val message = binding.messageInputField.text.toString()

            if (message.isEmpty()) {
                myToast("Введите текст в поле")
            } else sendMessage(message, user.id, TYPE_TEXT) {

                saveToMainList(user.id, TYPE_CHAT)
                binding.messageInputField.text.clear()
            }
        }

        binding.singleChatFragmentClipIcon.setOnClickListener {
            pickFromGallery()
        }

    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) getAlertDialogWithImage(uri)
        }

    val permissionLauncherSingle =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                myToast("Разрешение дано")
            } else {
                myToast("Разрешение не дано")
            }
        }

    private fun pickFromGallery() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            galleryLauncher.launch("image/*")
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            myToast("Оно необходимо чтобы отправить фото")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncherSingle.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissionLauncherSingle.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun getAlertDialogWithImage(imageUri: Uri?) {
        val layoutInflater = LayoutInflater.from(requireContext())
        val view = layoutInflater.inflate(R.layout.alert_dialog_attach_file, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val imageView = view.findViewById<ImageView>(R.id.singleChatAlertDialogImageView)
        builder.apply {
            setTitle("Прикрепить файл?")
            imageView.setImageURI(imageUri)
            setCancelable(false)
            setNegativeButton("Отмена") { _, _ -> }
            setPositiveButton("Отправить") { _, _ ->
                sendImageMessage(user.id, imageUri) { saveToMainList(user.id, TYPE_CHAT) }
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun initRecyclerView() {
        mAdapter = SingleChatAdapter { message ->
            if (message.type != TYPE_IMAGE) {
                getAlertDialogForTextTypeMessage(message)
            } else {
                getAlertDialogForImageTypeMessage(message)
            }
        }
        mRefMessages = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID).child(user.id)
        binding.singleChatFragmentRecyclerView.adapter = mAdapter
        binding.singleChatFragmentRecyclerView.setHasFixedSize(true)
        binding.singleChatFragmentRecyclerView.isNestedScrollingEnabled = false
        binding.singleChatFragmentRecyclerView.layoutManager = mLayoutManager

        mMessagesListener = AppChildEventListener {
            val message = it.getUserDataModel()

            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(message) {
                    binding.singleChatFragmentRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(message) {
                    binding.singleChatFragmentSwipeRefresh.isRefreshing = false
                }
            }

        }


        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)


        binding.singleChatFragmentRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }
        })

        binding.singleChatFragmentSwipeRefresh.setOnRefreshListener {
            updateData()
        }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

    }

    override fun onPause() {
        super.onPause()
        mRefMessages.removeEventListener(mMessagesListener)
        mRefUser.removeEventListener(mListenerInfoToolbar)

    }

    override fun onStop() {
        super.onStop()
        toolbar.visibility = View.VISIBLE
    }

    private fun initInfoToolbar() {

        if (mReceivingUser.firstname.isEmpty() && mReceivingUser.lastname.isEmpty()) {
            binding.toolbarUserFullName.text = mReceivingUser.email
        } else {
            binding.toolbarUserFullName.text =
                "${mReceivingUser.firstname} ${mReceivingUser.lastname}"
        }
        binding.toolbarUserStatus.text = mReceivingUser.state

        Glide.with(requireContext()).load(mReceivingUser.userPhotoUri)
            .into(binding.toolbarUserImage)

        binding.singleChatToolbarUpBackArrow.setOnClickListener {
            findNavController().navigate(R.id.chatsListFragment)
        }

        binding.aboutUserInfoButton.setOnClickListener {
            val action =
                SingleChatFragmentDirections.Companion.actionSingleChatFragmentToAboutUserFragment(
                    user
                )
            findNavController().navigate(action)
        }
    }

    private fun getAlertDialogForTextTypeMessage(message: UserData) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Что вы хотите выполнить?")
            setPositiveButton("Удалить") { _, _ ->
                removeMessage(
                    message,
                    user.id
                ) { mAdapter.removeMessageFromListAdapter(message) }
            }
            setNegativeButton("Отмена") { _, _ -> }
            create()
        }
        builder.show()
    }

    private fun getAlertDialogForImageTypeMessage(message: UserData) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Что вы хотите выполнить?")
            setPositiveButton("Удалить") { _, _ ->
                removeMessage(
                    message,
                    user.id
                ) { mAdapter.removeMessageFromListAdapter(message) }
            }
            setNeutralButton("Посмотреть") { _, _ ->
                fullScreenImageDialog(message)
            }
            setNegativeButton("Отмена") { _, _ -> {} }
            create()
        }
        builder.show()

    }

    private fun fullScreenImageDialog(message: UserData) {
        val dialog = FullScreenFragment(message)
        dialog.show(childFragmentManager, "custom")

    }


}


