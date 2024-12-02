package com.example.urbanmessenger.single_chat

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.CONTACT
import com.example.urbanmessenger.DATA_BASE_ROOT
import com.example.urbanmessenger.NODE_MESSAGES
import com.example.urbanmessenger.TYPE_TEXT
import com.example.urbanmessenger.UID
import com.example.urbanmessenger.databinding.FragmentSingleChatBinding
import com.example.urbanmessenger.getUserDataModel
import com.example.urbanmessenger.sendMessage
import com.example.urbanmessenger.utils.AppChildEventListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference

class SingleChatFragment : Fragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private var mListListeners = mutableListOf<AppChildEventListener>()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        initRecyclerView()

        binding.singleChatFragmentSendMessageIcon.setOnClickListener {
            mSmoothScrollToPosition = true

            val message = binding.messageInputField.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(requireContext(), "Введите сообщение", Toast.LENGTH_SHORT).show()
            } else sendMessage(message, CONTACT.id, TYPE_TEXT) {
                binding.messageInputField.text.clear()
            }
        }

    }

    private fun initRecyclerView() {
        mAdapter = SingleChatAdapter()
        mRefMessages = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID).child(CONTACT.id)
        binding.singleChatFragmentRecyclerView.adapter = mAdapter

        mMessagesListener = AppChildEventListener {
            mAdapter.addItem(it.getUserDataModel())
            if (mSmoothScrollToPosition) {
                binding.singleChatFragmentRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            }

        }


        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)

        binding.singleChatFragmentRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }

            private fun updateData() {
                mSmoothScrollToPosition = false
                mIsScrolling = false
                mCountMessages += 10
                mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
                mListListeners.add(mMessagesListener)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }
        })
    }


    override fun onPause() {
        super.onPause()
        mListListeners.forEach {
            mRefMessages.removeEventListener(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}