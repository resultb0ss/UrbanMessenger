package com.example.urbanmessenger.single_chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.UID
import com.example.urbanmessenger.databinding.ItemSingleMessageBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.asTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = emptyList<UserData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleChatHolder {
        val binding =
            ItemSingleMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SingleChatHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SingleChatHolder,
        position: Int
    ) {
        val message = mListMessagesCache[position]
        if (message.from == UID) {
            holder.binding.blockUserMessage.visibility = View.VISIBLE
            holder.binding.blockReceivedMessage.visibility = View.GONE
            holder.binding.chatUserMessage.text = message.text
            holder.binding.chatUserMessageTime.text = message.timestamp.toString().asTime()
        } else {
            holder.binding.blockUserMessage.visibility = View.GONE
            holder.binding.blockReceivedMessage.visibility = View.VISIBLE
            holder.binding.chatReceiverMessage.text = message.text
            holder.binding.chatReceivedMessageTime.text = message.timestamp.toString().asTime()
        }
    }

    override fun getItemCount() = mListMessagesCache.size


    class SingleChatHolder(val binding: ItemSingleMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<UserData>) {
        mListMessagesCache = list
        notifyDataSetChanged()
    }
}

