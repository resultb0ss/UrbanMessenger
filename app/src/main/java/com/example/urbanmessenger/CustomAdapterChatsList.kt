package com.example.urbanmessenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.databinding.ItemChatsListBinding

class CustomAdapterChatsList(var chatList: MutableList<ChatData>) :
    RecyclerView.Adapter<CustomAdapterChatsList.ChatsViewHolder>() {

    private var onChatClickListener: OnChatClickListener? = null

    interface OnChatClickListener {
        fun onChatClick(chat: ChatData, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatsViewHolder {

        val binding =
            ItemChatsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChatsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChatsViewHolder,
        position: Int
    ) {
        val chat = chatList[position]
        holder.binding.itemChatsContactNameTV.text = chat.nameUser
        holder.binding.itemChatsContactMessageTV.text = chat.message
        holder.binding.itemChatsMessageTimeTV.text = chat.time
        holder.binding.itemChatsContactImageIV.setImageResource(chat.userImage)

        holder.itemView.setOnClickListener {
            if (onChatClickListener != null) {
                onChatClickListener!!.onChatClick(chat, position)
            }
        }
    }

    override fun getItemCount() = chatList.size


    class ChatsViewHolder(val binding: ItemChatsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun setChatClickListener(onChatClickListener: OnChatClickListener) {
        this.onChatClickListener = onChatClickListener
    }

    fun updateAdapter(newList: MutableList<ChatData>) {
        chatList = newList
    }
}