package com.example.urbanmessenger.utilits

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AppChildEventListenerNewMessage(
    val functionAdded: (DataSnapshot) -> Unit,
    val functionChanged: (DataSnapshot) -> Unit
) : ChildEventListener {

    override fun onChildAdded(
        snapshot: DataSnapshot,
        previousChildName: String?
    ) {
        functionAdded(snapshot)
    }

    override fun onChildChanged(
        snapshot: DataSnapshot,
        previousChildName: String?
    ) {
        functionChanged(snapshot)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
    }

    override fun onChildMoved(
        snapshot: DataSnapshot,
        previousChildName: String?
    ) {
    }

    override fun onCancelled(error: DatabaseError) {
    }

}