package com.example.urbanmessenger.single_chat

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.APP_ACTIVITY
import com.example.urbanmessenger.CONTACT
import com.example.urbanmessenger.R
import com.example.urbanmessenger.TYPE_CHAT
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_MESSAGES
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.TYPE_TEXT
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.data.network.saveToMainList
import com.example.urbanmessenger.data.network.sendMessage
import com.example.urbanmessenger.databinding.FragmentSingleChatBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppChildEventListener
import com.example.urbanmessenger.utils.AppTextWatcher
import com.example.urbanmessenger.utils.AppValueEventListener
import com.example.urbanmessenger.utils.myToast
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DatabaseReference

class SingleChatFragment : Fragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 15
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserData
    private lateinit var mRefUser: DatabaseReference
    private lateinit var toolbar: MaterialToolbar



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        toolbar = APP_ACTIVITY.findViewById<MaterialToolbar>(R.id.mainActivityToolbar)
        toolbar.visibility = View.GONE
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserDataModel()
            initInfoToolbar()
        }

        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(CONTACT.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)

        initFields()
        initRecyclerView()


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
                Toast.makeText(requireContext(), "Введите сообщение", Toast.LENGTH_SHORT).show()
            } else sendMessage(message, CONTACT.id, TYPE_TEXT) {

                saveToMainList(CONTACT.id, TYPE_CHAT)
                binding.messageInputField.text.clear()
            }
        }

        binding.singleChatFragmentClipIcon.setOnClickListener { attachFile() }

    }


    private fun initRecyclerView() {
        mAdapter = SingleChatAdapter{ message -> getAlertDialog(message)}
        mRefMessages = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID).child(CONTACT.id)
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

    private fun attachFile() {

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
        toolbar.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initInfoToolbar() {

        if (mReceivingUser.firstname.isEmpty() && mReceivingUser.lastname.isEmpty()) {
            binding.toolbarUserFullName.text = mReceivingUser.email
        } else {
            binding.toolbarUserFullName.text =
                "${mReceivingUser.firstname} ${mReceivingUser.lastname}"
        }
        binding.toolbarUserStatus.text = mReceivingUser.state

        binding.singleChatToolbarUpBackArrow.setOnClickListener {
            findNavController().navigate(R.id.chatsListFragment)
        }

        binding.aboutUserInfoButton.setOnClickListener {
            findNavController().navigate(R.id.aboutUserFragment)
        }
    }

    private fun getAlertDialog(message: UserData){
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Что вы хотите выполнить?")
            setPositiveButton("Удалить"){_,_-> myToast("Сообщение удалено ${message.text}")}
            setNegativeButton("Отмена"){_,_->}
            show()
        }



    }


}


