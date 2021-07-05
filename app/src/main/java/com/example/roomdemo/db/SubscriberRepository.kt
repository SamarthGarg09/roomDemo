package com.example.roomdemo.db

class SubscriberRepository(private val dao:SubscribersDAO) {
    val subscriber = dao.selectAll()
    suspend fun insert(subscriber: Subscribers){
        dao.insertSubscribers(subscriber)
    }
    suspend fun delete(subscriber: Subscribers){
       dao.deleteSubs(subscriber)
    }
    suspend fun update(subscriber: Subscribers){
        dao.updateSubscribers(subscriber)
    }
    suspend fun deleteAll(){
        dao.deleteAll()
    }

}