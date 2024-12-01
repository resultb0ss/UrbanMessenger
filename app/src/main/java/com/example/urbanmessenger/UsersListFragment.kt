package com.example.urbanmessenger

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.databinding.FragmentUsersListBinding
import com.example.urbanmessenger.databinding.ItemUsersListBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppValueEventListener
import com.example.urbanmessenger.utils.updateToolbar
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference


class UsersListFragment : Fragment() {


    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: FirebaseRecyclerAdapter<UserData, ContactsHolder>
    private lateinit var mRefContact: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefContactsListener: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach{
            it.key.removeEventListener(it.value)
        }
        Log.d("@@@", "OnViewPause")
    }

    override fun onResume() {
        super.onResume()
        updateToolbar("Пользователи", requireActivity())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRefUsers = DATA_BASE_ROOT.child(NODE_USERS)

        val options = FirebaseRecyclerOptions.Builder<UserData>().setQuery(
            mRefUsers,
            UserData::class.java
        ).build()

        mAdapter = object : FirebaseRecyclerAdapter<UserData, ContactsHolder>(options) {
            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: UserData
            ) {

                mRefContact = DATA_BASE_ROOT.child(NODE_USERS).child(model.id)

                mRefContactsListener  = AppValueEventListener {
                    val user = it.getValue(UserData::class.java) ?: UserData()
                    holder.binding.itemUsersContactNameTV.text = user.id
                    holder.binding.itemUsersContactEmailTV.text = user.email
                }

                mRefContact.addValueEventListener(mRefContactsListener)
                mapListeners[mRefContact] = mRefContactsListener
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ContactsHolder {

                val binding =
                    ItemUsersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                return ContactsHolder(binding)
            }

        }

        binding.usersListRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactsHolder(val binding: ItemUsersListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("@@@", "OnDestroyView")
        _binding = null

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("@@@", "OnDestroy")
    }


}