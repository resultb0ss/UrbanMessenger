package com.example.urbanmessenger.notifications

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=BOb4BVUOcDzrvnHDP6Apz5ZCMsGL9wezxISe4wFix1t3tHTs5ODdaj-uXCNX6RKICxvOvXYN3PnhMi2fBWIRkUs" // Your server key refer to video for finding your server key
    )
    @POST("/v1/com.example.urbanmessenger/messages:send")
    fun sendNotification(@Body body: NotificationSender?): Call<MyResponse?>?
}