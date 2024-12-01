package com.example.urbanmessenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.databinding.FragmentChatsBinding
import com.google.android.material.tabs.TabLayoutMediator


class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PageAdapter(this)
        binding.chatsFragmentViewPager.adapter = adapter

        TabLayoutMediator(
            binding.chatsFragmentTabLayout,
            binding.chatsFragmentViewPager
        ) { tab, position ->

            val tabNames = listOf("Чаты", "Пользователи")
            tab.text = tabNames[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}