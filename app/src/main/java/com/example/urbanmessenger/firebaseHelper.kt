package com.example.urbanmessenger

import android.content.Context
import android.widget.Toast
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.myToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

lateinit var AUTHFIREBASE: FirebaseAuth
lateinit var DATA_BASE_ROOT: DatabaseReference
lateinit var USER: UserData
lateinit var UID: String

//Nodes
const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_MESSAGES = "messages"

const val TYPE_TEXT = "text"

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

fun initFirebase() {
    AUTHFIREBASE = FirebaseAuth.getInstance()
    DATA_BASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserData()
    UID = AUTHFIREBASE.currentUser?.uid.toString()
}

fun DataSnapshot.getUserDataModel(): UserData =
    this.getValue(UserData::class.java) ?: UserData()

fun sendMessage(
    message: String,
    receivingUserId: String,
    typeText: String,
    function: () -> Unit
) {

    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"
    val messageKey = DATA_BASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    DATA_BASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }


}



