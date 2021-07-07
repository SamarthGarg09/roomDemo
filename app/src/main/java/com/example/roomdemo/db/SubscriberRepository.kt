package com.example.roomdemo.db

class SubscriberRepository(private val dao:SubscribersDAO) {
    val subscriber = dao.selectAll()
    suspend fun insert(subscriber: Subscribers):Long{
        return dao.insertSubscribers(subscriber)
    }
    suspend fun delete(subscriber: Subscribers):Int{
       return dao.deleteSubs(subscriber)
    }
    suspend fun update(subscriber: Subscribers):Int{
       return dao.updateSubscribers(subscriber)
    }
    suspend fun deleteAll(){
        dao.deleteAll()
    }

}