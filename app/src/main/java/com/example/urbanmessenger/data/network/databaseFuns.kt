package com.example.urbanmessenger.data.network

import android.net.Uri
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utilits.AppValueEventListener
import com.example.urbanmessenger.utilits.myToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

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
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    DATA_BASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }


}

fun removeMessage(
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
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    DATA_BASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }


}

fun sendImageMessage(
    receivingUserId: String,
    imageUri: Uri?,
    function: () -> Unit
) {

    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"
    val messageKey = DATA_BASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = TYPE_IMAGE
    mapMessage[CHILD_TEXT] = "Фотография"
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_IMAGE_URI_SENDER] = imageUri.toString()
    mapMessage[CHILD_IMAGE_URI_RECEIVER] = "placeholder"
    mapMessage[CHILD_IMAGE_URI_SENDER] = imageUri.toString()


    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    DATA_BASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }

}

fun saveToMainList(id: String, type: String) {
    var refUser = "$NODE_MAIN_LIST/$UID/$id"
    var refReceived = "$NODE_MAIN_LIST/$id/$UID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = type

    mapReceived[CHILD_ID] = UID
    mapReceived[CHILD_TYPE] = type

    val commonMap = hashMapOf<String, Any>()

    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    DATA_BASE_ROOT.updateChildren(commonMap).addOnFailureListener { myToast(it.message.toString()) }
}

fun updateProfileInfo(
    newFirstName: String,
    newLastName: String,
    newProfession: String,
    newAge: String,
    newAddress: String,
    onSuccess: () -> Unit
) {

    updateFirstName(newFirstName)
    updateLastName(newLastName)
    updateAge(newAge)
    updateAddress(newAddress)
    updateProfession(newProfession)

    onSuccess()
}

private fun updateProfession(newProfession: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PROFESSION).setValue(newProfession)
        .addOnSuccessListener {
            USER.profession = newProfession
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

private fun updateAddress(newAddress: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_ADDRESS).setValue(newAddress)
        .addOnSuccessListener {
            USER.address = newAddress
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

private fun updateAge(newAge: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_AGE).setValue(newAge)
        .addOnSuccessListener {
            USER.age = newAge
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

private fun updateLastName(newLastName: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_LASTNAME).setValue(newLastName)
        .addOnSuccessListener {
            USER.lastname = newLastName
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

private fun updateFirstName(newFirstName: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FIRSTNAME).setValue(newFirstName)
        .addOnSuccessListener {
            USER.firstname = newFirstName
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

fun updatePhone(newPhone: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHONE).setValue(newPhone)
        .addOnSuccessListener {
            USER.phone = newPhone
            myToast("Номер успешно добавлен")
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

fun updatePhotoUri(newUri: String) {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_IMAGE_URI).setValue(newUri)
        .addOnSuccessListener {
            USER.userPhotoUri = newUri
            myToast("Фото успешно обновлено")
        }
        .addOnFailureListener { myToast(it.message?.toString().toString()) }
}

fun initUser() {
    DATA_BASE_ROOT.child(NODE_USERS).child(UID).addListenerForSingleValueEvent(
        AppValueEventListener {
            USER = it.getValue(UserData::class.java) ?: UserData()
        }
    )
}





