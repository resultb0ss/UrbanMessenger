package com.example.urbanmessenger

import java.io.Serializable

class ChatData(val nameUser: String, val message: String, val time: String, val userImage: Int): Serializable {
}