package com.example.urbanmessenger.chats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.APP_ACTIVITY
import com.example.urbanmessenger.database.DATA_BASE_ROOT
import com.example.urbanmessenger.database.NODE_MAIN_LIST
import com.example.urbanmessenger.database.NODE_MESSAGES
import com.example.urbanmessenger.database.NODE_USERS
import com.example.urbanmessenger.database.UID
import com.example.urbanmessenger.database.getUserDataModel
import com.example.urbanmessenger.databinding.FragmentChatsListBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppValueEventListener

class ChatsListFragment : Fragment() {

    private var _binding: FragmentChatsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ChatsListAdapter
    private val mRefMainList = DATA_BASE_ROOT.child(NODE_MAIN_LIST).child(UID)
    private val mRefUser = DATA_BASE_ROOT.child(NODE_USERS)
    private val mRefMessages = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID)
    private var mListItems = listOf<UserData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsListBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.updateToolbarTitle("Чаты")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = ChatsListAdapter()
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getUserDataModel() }
            mListItems.forEach { model ->

                mRefUser.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                        val newModel = dataSnapshot1.getUserDataModel()

                        mRefMessages.child(model.id).limitToLast(1).addListenerForSingleValueEvent(
                            AppValueEventListener { dataSnapshot2 ->
                                val tempList = dataSnapshot2.children.map { it.getUserDataModel() }
                                newModel.lastMessage = tempList[0].text
                                mAdapter.updateListItems(newModel)
                            })
                    })
            }
        })

        binding.chatsListRecyclerView.adapter = mAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}