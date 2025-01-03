package com.example.urbanmessenger.data.network

import com.example.urbanmessenger.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging

lateinit var AUTHFIREBASE: FirebaseAuth
lateinit var DATA_BASE_ROOT: DatabaseReference
lateinit var USER: UserData
lateinit var UID: String
lateinit var FIREBASEMESSAGING: FirebaseMessaging

//Nodes
const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_MESSAGES = "messages"
const val NODE_SUPPORTS_MESSAGES = "messagesToSupport"
const val NODE_TOKEN = "token"

const val TYPE_TEXT = "text"
const val TYPE_IMAGE = "image"
const val TYPE_PLACEHOLDER = "placeholder"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_IMAGE_URI = "userPhotoUri"
const val CHILD_FIRSTNAME = "firstname"
const val CHILD_LASTNAME = "lastname"
const val CHILD_MAIL = "email"
const val CHILD_AGE = "age"
const val CHILD_ADDRESS = "address"
const val CHILD_PROFESSION = "profession"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timestamp"
const val NODE_MAIN_LIST = "main_list"
const val CHILD_IMAGE_URI_SENDER = "imageUriSender"
const val CHILD_IMAGE_URI_RECEIVER = "imageUriReceiver"