package com.example.urbanmessenger.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.R
import com.example.urbanmessenger.databinding.FragmentChatsBinding
import com.google.android.material.tabs.TabLayout


class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (binding.chatsFragmentContainer.isEmpty()) {
            parentFragmentManager.beginTransaction().replace(
                R.id.chatsFragmentContainer,
                ChatsListFragment()
            ).commit()
        }


        binding.chatsFragmentTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> parentFragmentManager.beginTransaction().replace(
                        R.id.chatsFragmentContainer,
                        ChatsListFragment()
                    ).commit()

                    else -> parentFragmentManager.beginTransaction().replace(
                        R.id.chatsFragmentContainer,
                        UsersListFragment()
                    ).commit()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}