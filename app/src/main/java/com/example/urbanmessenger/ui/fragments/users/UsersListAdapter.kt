package com.example.urbanmessenger.ui.fragments.users


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanmessenger.databinding.ItemUsersListBinding
import com.example.urbanmessenger.models.UserData
import android.R

class UsersListAdapter(private val onClick: (UserData) -> Unit) :
    RecyclerView.Adapter<UsersListAdapter.UsersListHolder>() {

    val listItems = mutableListOf<UserData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersListHolder {
        val binding =
            ItemUsersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UsersListHolder(binding)
    }


    override fun onBindViewHolder(
        holder: UsersListHolder,
        position: Int
    ) {
        val user = listItems[position]
        if (user.firstname.isEmpty() && user.lastname.isEmpty()) {
            holder.binding.itemUsersContactNameTV.text = user.email.toString()
        } else {
            holder.binding.itemUsersContactNameTV.text = "${user.firstname} ${user.lastname}"
        }
        holder.binding.itemUsersContactEmailTV.text = user.state
        holder.itemView.setOnClickListener {
            onClick(user)
        }
    }

    fun updateListItems(item: UserData) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    override fun getItemCount() = listItems.size


    class UsersListHolder(val binding: ItemUsersListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


}