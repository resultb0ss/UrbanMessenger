package com.example.urbanmessenger.notifications

class NotificationSender(val data: Data?, val to: String) {
    constructor() : this(null, "") {}
}