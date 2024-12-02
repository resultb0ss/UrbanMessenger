package com.example.urbanmessenger.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.databinding.ItemChatsListBinding
import com.example.urbanmessenger.models.UserData

class ChatsListAdapter(private val onClick: (UserData) -> Unit) :
    RecyclerView.Adapter<ChatsListAdapter.ChatsListHolder>() {

    val listItems = mutableListOf<UserData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatsListHolder {
        val binding =
            ItemChatsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChatsListHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChatsListHolder,
        position: Int
    ) {
        val chat = listItems[position]
        if (chat.firstname.isEmpty() && chat.lastname.isEmpty()) {
            holder.binding.itemChatsContactNameTV.text = chat.email
        } else {"${chat.firstname} ${chat.lastname}"}

        holder.binding.itemChatsMessageTimeTV.text = chat.timestamp.toString()
        holder.binding.itemChatsContactMessageTV.text = chat.lastMessage
        holder.itemView.setOnClickListener {
            onClick(chat)
        }
    }

    fun updateListItems(item: UserData) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    override fun getItemCount() = listItems.size


    class ChatsListHolder(val binding: ItemChatsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}