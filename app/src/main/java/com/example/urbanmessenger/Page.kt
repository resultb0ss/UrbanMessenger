package com.example.urbanmessenger

import androidx.fragment.app.Fragment
import java.io.Serializable

class Page(val label: String, val fragment: Fragment) : Serializable {

    companion object {
        val pages = mutableListOf(
            Page("Чаты", ChatsListFragment()),
            Page("Пользователи", UsersListFragment())
        )
    }
}