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
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.CONTACT
import kotlinx.coroutines.launch


class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: UsersListAdapter
    private lateinit var fAdapter: UsersListAdapter
    private var mRefUsers = DATA_BASE_ROOT.child(NODE_USERS)
    private var mListItems = listOf<UserData>()
    private var filteredList = listOf<UserData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()

    }

    private fun initSearchBar() {
        fAdapter = UsersListAdapter { user -> navigateToSingleChatFragment(user) }
        binding.filteredUsersListRecyclerView.adapter = fAdapter
        binding.usersListSearchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                Log.d("@@@","on changed ${p0}")
//                val listForSearchBar =
//                    filteredList.filter { it.email.contains(p0!!, ignoreCase = true) }
//                listForSearchBar.forEach { user -> fAdapter.updateListItems(user) }
//                Log.d("@@@","${listForSearchBar.forEach { user -> fAdapter.updateListItems(user) }}")
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }


    private fun initRecyclerView() {
        mAdapter = UsersListAdapter { user -> navigateToSingleChatFragment(user) }

        lifecycleScope.launch {
            mRefUsers.addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
                mListItems = snapshot.children.map { it.getUserDataModel() }
                filteredList = mListItems.filter { it -> it.id != USER.id }
                filteredList.forEach { user -> mAdapter.updateListItems(user) }
            })
        }

        binding.usersListRecyclerView.adapter = mAdapter
    }

    private fun navigateToSingleChatFragment(user: UserData) {
        CONTACT = user
        findNavController().navigate(R.id.action_usersListFragment_to_singleChatFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}