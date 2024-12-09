package com.example.urbanmessenger

import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessageDto
    )

}