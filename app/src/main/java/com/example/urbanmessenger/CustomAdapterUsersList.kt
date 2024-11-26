package com.example.urbanmessenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.databinding.ItemUsersListBinding

class CustomAdapterUsersList(var users: MutableList<UserData>) :
    RecyclerView.Adapter<CustomAdapterUsersList.UserViewHolder>() {

        private var onUserClickListener: OnUserClickListener? = null

    interface OnUserClickListener{
        fun onUserClick(user: UserData, position: Int)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val binding =
            ItemUsersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = users[position]
        holder.binding.itemUsersContactNameTV.text = user.name
        holder.binding.itemUsersContactEmailTV.text = user.mail
        holder.binding.itemUsersContactImageIV.setImageResource(user.userImage)
        holder.itemView.setOnClickListener{
            if (onUserClickListener != null){
                onUserClickListener!!.onUserClick(user, position)
            }
        }
    }

    override fun getItemCount() = users.size


    class UserViewHolder(val binding: ItemUsersListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun setUserClickListener(onUserClickListener: OnUserClickListener){
        this.onUserClickListener = onUserClickListener
    }

    fun updateAdapter(newList: MutableList<UserData>){
        users = newList
    }


}