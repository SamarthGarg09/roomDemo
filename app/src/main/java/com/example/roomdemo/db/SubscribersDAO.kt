package com.example.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface SubscribersDAO{

    @Insert
    suspend fun insertSubscribers(subscribers: Subscribers)
    @Update
    suspend fun updateSubscribers(subscribers: Subscribers)
    @Delete
    suspend fun deleteSubs(subscribers: Subscribers)
    @Query("DELETE FROM subscribers_data_table")
    suspend fun deleteAll()
    @Query("SELECT * FROM subscribers_data_table")
    fun selectAll():LiveData<List<Subscribers>>
}