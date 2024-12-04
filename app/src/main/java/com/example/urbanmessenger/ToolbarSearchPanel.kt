package com.example.urbanmessenger

import android.view.View
import android.widget.EditText
import android.widget.ImageView

class ToolbarSearchPanel(var searchItem: Boolean) {

    lateinit var searchIcon: ImageView
    lateinit var searchField: EditText


    fun searchFieldDisable() {
        searchIcon.visibility = View.GONE
        searchField.visibility = View.GONE
        searchField.text.clear()
        searchItem = true
    }

    fun initToolbarSearchIcon() {
        searchIcon = APP_ACTIVITY.findViewById<ImageView>(R.id.mainToolbarSearchIcon)
        searchField = APP_ACTIVITY.findViewById<EditText>(R.id.toolbarSearchField)
        searchIcon.visibility = View.VISIBLE
        searchIcon.setOnClickListener{
            if (!searchItem){
                searchFieldOff()
            } else {
                searchFieldOn()
            }
        }

    }

    private fun searchFieldOn(){
        searchField.visibility = View.VISIBLE
        searchItem = false
    }

    private fun searchFieldOff(){
        searchField.visibility = View.GONE
        searchField.text.clear()
        searchItem = true
    }
}