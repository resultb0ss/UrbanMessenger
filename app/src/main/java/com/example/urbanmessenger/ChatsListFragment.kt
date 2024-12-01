package com.example.urbanmessenger

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.databinding.FragmentChatsListBinding
import com.example.urbanmessenger.utils.updateToolbar

class ChatsListFragment : Fragment() {

    private var _binding: FragmentChatsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterChatsList: CustomAdapterChatsList

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

        val chatsList = mutableListOf<ChatData>(
            ChatData("Виктор Иванов", "Привет, как ты?", "20:15", R.drawable.man),
            ChatData("Сергей Петров", "Отправьте документы пожалуйста", "20:20", R.drawable.man),
            ChatData("Екатерина Зимина", "Вы мне не написали вчера", "20:25", R.drawable.man),
            ChatData("Иван Туйков", "Привет, я звонил тебе ты не ответил", "20:26", R.drawable.man),
        )

        adapterChatsList = CustomAdapterChatsList(chatsList)
        binding.chatsListRecyclerView.adapter = adapterChatsList
        adapterChatsList.notifyDataSetChanged()

        adapterChatsList.setChatClickListener(
            object : CustomAdapterChatsList.OnChatClickListener {
                override fun onChatClick(chat: ChatData, position: Int) {

                    val intent = Intent(requireActivity(), SingleChatActivity::class.java)
                    intent.putExtra("name", chat.nameUser)
                    startActivity(intent)

                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        updateToolbar("Чаты", requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}