package com.example.urbanmessenger

import androidx.recyclerview.widget.DiffUtil
import com.example.urbanmessenger.models.UserData


class DiffUtilCallback(
    private val oldList: List<UserData>,
    private val newList: List<UserData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}