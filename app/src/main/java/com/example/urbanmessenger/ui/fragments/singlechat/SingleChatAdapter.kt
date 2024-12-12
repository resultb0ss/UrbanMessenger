package com.example.urbanmessenger.ui.fragments.singlechat

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.data.network.TYPE_IMAGE
import com.example.urbanmessenger.data.network.TYPE_TEXT
import com.example.urbanmessenger.data.network.UID
import com.example.urbanmessenger.databinding.ItemSingleMessageBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utilits.asTime

class SingleChatAdapter(val onClick: (message: UserData) -> Unit) :
    RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<UserData>()

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
        when (message.type) {
            TYPE_TEXT -> {
                if (message.from == UID) {
                    holder.binding.blockUserMessage.visibility = View.VISIBLE
                    holder.binding.blockReceivedMessage.visibility = View.GONE
                    holder.binding.chatUserMessage.text = message.text
                    holder.binding.chatUserMessageTime.text = message.timestamp.toString().asTime()
                } else {
                    holder.binding.blockUserMessage.visibility = View.GONE
                    holder.binding.blockReceivedMessage.visibility = View.VISIBLE
                    holder.binding.chatReceiverMessage.text = message.text
                    holder.binding.chatReceivedMessageTime.text =
                        message.timestamp.toString().asTime()
                }
            }

            TYPE_IMAGE -> {
                if (message.from == UID) {
                    holder.binding.blockUserMessage.visibility = View.VISIBLE
                    holder.binding.blockReceivedMessage.visibility = View.GONE
                    holder.binding.chatUserMessage.visibility = View.GONE
                    holder.binding.chatUserMessageImage.visibility = View.VISIBLE
                    holder.binding.chatUserMessageImage.setImageURI(message.imageUriSender.toUri())
                    holder.binding.chatUserMessageTime.text = message.timestamp.toString().asTime()
                } else {
                    holder.binding.blockUserMessage.visibility = View.GONE
                    holder.binding.blockReceivedMessage.visibility = View.VISIBLE
                    holder.binding.chatReceiverMessage.visibility = View.GONE
                    holder.binding.chatReceiverMessageImage.visibility = View.VISIBLE
                    holder.binding.chatReceiverMessageImage.setImageURI(message.imageUriSender.toUri())
                    holder.binding.chatReceivedMessageTime.text =
                        message.timestamp.toString().asTime()
                }
            }

        }
        holder.itemView.setOnClickListener{onClick(message)}





    }

    private fun searchIndex(messages: MutableList<UserData>, message: UserData): Int{
        var result = -1
        for (i in messages.indices){
            if (message.text == messages[i].text && message.timestamp == messages[i].timestamp) result = i
        }
        return result

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeMessageFromListAdapter(message: UserData){

        val index = searchIndex(mListMessagesCache,message)
        mListMessagesCache.removeAt(index)
        notifyDataSetChanged()
    }

    override fun getItemCount() = mListMessagesCache.size

    class SingleChatHolder(val binding: ItemSingleMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun addItemToBottom(item: UserData, onSuccess: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: UserData, onSuccess: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timestamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }

}


