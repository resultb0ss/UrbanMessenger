package com.example.urbanmessenger.ui.fragments.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urbanmessenger.models.UserData

class ChatsViewModel: ViewModel() {

    private val _chats = MutableLiveData<List<UserData>>()

    val chats: LiveData<List<UserData>> = _chats

    


}