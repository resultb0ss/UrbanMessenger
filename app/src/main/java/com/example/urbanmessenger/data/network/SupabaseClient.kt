package com.example.urbanmessenger.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.JacksonSerializer
import io.github.jan.supabase.storage.Storage

object SupabaseClient {

    val supabaseClient = createSupabaseClient(
        supabaseKey = BuildConfig.SUPABASE_KEY,
        supabaseUrl = BuildConfig.SUPABASE_URL
    ) {
        defaultSerializer = JacksonSerializer()
        install(Storage)
        install(Postgrest)
    }
}