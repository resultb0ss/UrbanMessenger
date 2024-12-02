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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.CONTACT
import com.example.urbanmessenger.database.DATA_BASE_ROOT
import com.example.urbanmessenger.database.NODE_MESSAGES
import com.example.urbanmessenger.TYPE_CHAT
import com.example.urbanmessenger.database.TYPE_TEXT
import com.example.urbanmessenger.database.UID
import com.example.urbanmessenger.databinding.FragmentSingleChatBinding
import com.example.urbanmessenger.database.getUserDataModel
import com.example.urbanmessenger.database.saveToMainList
import com.example.urbanmessenger.database.sendMessage
import com.example.urbanmessenger.utils.AppChildEventListener
import com.example.urbanmessenger.utils.AppTextWatcher
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

        initFields()
        initRecyclerView()


    }

    private fun initFields() {
        mLayoutManager = LinearLayoutManager(this.context)

        binding.messageInputField.addTextChangedListener(AppTextWatcher{
            val string = binding.messageInputField.text.toString()
            if (string.isEmpty()){
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

        binding.singleChatFragmentClipIcon.setOnClickListener{ attachFile() }

    }


    private fun initRecyclerView() {
        mAdapter = SingleChatAdapter()
        mRefMessages = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID).child(CONTACT.id)
        binding.singleChatFragmentRecyclerView.adapter = mAdapter
        binding.singleChatFragmentRecyclerView.setHasFixedSize(true)
        binding.singleChatFragmentRecyclerView.isNestedScrollingEnabled = false
        binding.singleChatFragmentRecyclerView.layoutManager = mLayoutManager

        mMessagesListener = AppChildEventListener {
            val message = it.getUserDataModel()

            if (mSmoothScrollToPosition){
                mAdapter.addItemToBottom(message){
                    binding.singleChatFragmentRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(message){
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

        binding.singleChatFragmentSwipeRefresh.setOnRefreshListener{
            updateData()
        }
    }

    private fun attachFile(){

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}