package com.example.urbanmessenger.ui.fragments.users

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.urbanmessenger.R
import com.example.urbanmessenger.data.network.DATA_BASE_ROOT
import com.example.urbanmessenger.data.network.NODE_USERS
import com.example.urbanmessenger.data.network.USER
import com.example.urbanmessenger.data.network.getUserDataModel
import com.example.urbanmessenger.databinding.FragmentUsersListBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.ui.fragments.BaseFragment
import com.example.urbanmessenger.ui.fragments.chats.ChatsListSearchViewAdapter
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.CONTACT
import kotlinx.coroutines.launch


class UsersListFragment : BaseFragment<FragmentUsersListBinding>() {

    private lateinit var mAdapter: UsersListAdapter
    private lateinit var sAdapter: UsersListSearchViewAdapter
    private var mRefUsers = DATA_BASE_ROOT.child(NODE_USERS)
    private var mListItems = listOf<UserData>()
    private var filteredList = listOf<UserData>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUsersListBinding {
        return FragmentUsersListBinding.inflate(inflater,container,false)
    }


    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initSearchView()
    }

    private fun initSearchView() {
        binding.usersListSearchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?, p1: Int, p2: Int, p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?, p1: Int, p2: Int, p3: Int
            ) {
                val query = p0.toString()
                filterChats(query)
                Log.d("@@@","${filterChats(query)}")
            }

            override fun afterTextChanged(p0: Editable?) {}
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
        mAdapter = UsersListAdapter { user -> navigateToSingleChatFragment(user) }
        sAdapter = UsersListSearchViewAdapter { user -> navigateToSingleChatFragment(user) }


        lifecycleScope.launch {
            mRefUsers.addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
                mListItems = snapshot.children.map { it.getUserDataModel() }
                filteredList = mListItems.filter { it -> it.id != USER.id }
                filteredList.forEach { user -> mAdapter.updateListItems(user) }
            })
        }

        binding.filteredUsersListRecyclerView.adapter = sAdapter
        binding.usersListRecyclerView.adapter = mAdapter
    }

    private fun navigateToSingleChatFragment(user: UserData) {
        CONTACT = user
        findNavController().navigate(R.id.action_usersListFragment_to_singleChatFragment)
    }

}