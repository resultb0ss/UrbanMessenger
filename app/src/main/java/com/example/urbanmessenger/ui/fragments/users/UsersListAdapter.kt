package com.example.urbanmessenger.ui.fragments.users


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.urbanmessenger.R
import com.example.urbanmessenger.databinding.ItemUsersListBinding
import com.example.urbanmessenger.models.UserData

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

        if (user.userPhotoUri.isEmpty()) {
            holder.binding.itemUsersContactImageIV
                .setImageResource(R.drawable.baseline_account_circle_24)
        } else {
            Glide.with(holder.itemView.context).load(user.userPhotoUri)
                .into(holder.binding.itemUsersContactImageIV)
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