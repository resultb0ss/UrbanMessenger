//package com.example.urbanmessenger.data.local
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Update
//import com.example.urbanmessenger.models.UserData
//
//
//@Dao
//interface UserDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUser(user: UserData)
//
//    @Delete
//    suspend fun deleteUser(user: UserData)
//
//    @Update
//    fun updateUser(user: UserData)
//
//}