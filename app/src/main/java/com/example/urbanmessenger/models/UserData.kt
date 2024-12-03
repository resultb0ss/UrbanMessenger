package com.example.urbanmessenger.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

//@Entity(tableName = "user")
data class UserData(
//    @PrimaryKey
    val id: String = "",
    var username: String = "",
    var phone: String = "",
    var userPhotoUri: String = "",
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var age: String = "",
    var address: String = "",
    var profession: String = "",
    var state: String = "",

    //Messages property
    var text: String = "",
    var type: String = "",
    var from: String = "",

    var timestamp: Any = "",

    var lastMessage: String = ""

) : Serializable {
    override fun equals(other: Any?): Boolean {
        return (other as UserData).id == id
    }




}


