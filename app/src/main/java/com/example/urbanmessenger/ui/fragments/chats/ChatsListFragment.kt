package com.example.urbanmessenger.ui.fragments.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_MAIN_LIST
import com.example.urbanmessenger.data.network.NODE_MESSAGES
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.databinding.FragmentChatsListBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.utilits.APP_ACTIVITY
import com.example.urbanmessenger.utilits.AppTextWatcher
import com.example.urbanmessenger.utilits.AppValueEventListener
import kotlinx.coroutines.launch

class ChatsListFragment : BaseFragment<FragmentChatsListBinding>() {

    private lateinit var mAdapter: ChatsListAdapter
    private lateinit var sAdapter: ChatsListSearchViewAdapter
    private val mRefMainList = DATA_BASE_ROOT.child(NODE_MAIN_LIST).child(UID)
    private val mRefUser = DATA_BASE_ROOT.child(NODE_USERS)
    private val mRefMessages = DATA_BASE_ROOT.child(NODE_MESSAGES).child(UID)
    private var mListItems = listOf<UserData>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatsListBinding {
        return FragmentChatsListBinding.inflate(inflater, container, false)
    }


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.updateToolbarTitle("Чаты")
        initRecyclerView()
        initSearchView()


    }


    private fun initSearchView() {
        binding.chatsListSearchView.editText.addTextChangedListener(AppTextWatcher { charSequence ->
            val query = charSequence.toString()
            filterChats(query)
        })

    }

    private fun filterChats(query: String) {

        val filteredList = mAdapter.listItems.filter {
            it.firstname.contains(query, ignoreCase = true) ||
                    it.lastname.contains(query, ignoreCase = true) || it.email.contains(
                query,
                ignoreCase = true
            )
        }

        sAdapter.listItems.clear()
        sAdapter.listItems.addAll(filteredList)
        sAdapter.notifyDataSetChanged()
    }


    private fun initRecyclerView() {
        mAdapter = ChatsListAdapter { user -> navigateToSingleChatFragment(user) }
        sAdapter = ChatsListSearchViewAdapter { user -> navigateToSingleChatFragment(user) }

        lifecycleScope.launch {
            mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                mListItems = dataSnapshot.children.map { it.getUserDataModel() }
                mListItems.forEach { model ->

                    mRefUser.child(model.id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                            val newModel = dataSnapshot1.getUserDataModel()

                            mRefMessages.child(model.id).limitToLast(1)
                                .addListenerForSingleValueEvent(
                                    AppValueEventListener { dataSnapshot2 ->

                                        val tempList =
                                            dataSnapshot2.children.map { it.getUserDataModel() }
                                        if (tempList.isEmpty()) {
                                            newModel.lastMessage = "Чат очищен"
                                        } else {
                                            newModel.lastMessage = tempList[0].text
                                        }

                                        if (!mAdapter.listItems.contains(newModel)) {
                                            mAdapter.updateListItems(newModel)
                                        }
                                    })
                        })
                }
            })
        }

        binding.chatsListRecyclerView.adapter = mAdapter
        binding.filteredChatsListRecyclerView.adapter = sAdapter
    }

    private fun navigateToSingleChatFragment(user: UserData) {
        val action =
            ChatsListFragmentDirections.Companion.actionChatsListFragmentToSingleChatFragment(user)
        findNavController().navigate(action)
    }

}