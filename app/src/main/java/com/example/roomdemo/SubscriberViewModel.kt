package com.example.roomdemo


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.SubscriberRepository
import com.example.roomdemo.db.Subscribers
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository:SubscriberRepository):ViewModel(), Observable {

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete:Subscribers
    val subscribers = repository.subscriber
    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButton = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteButton = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
    get() = statusMessage
    init{
        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear All"
    }
    fun saveOrUpdate(){

        if(isUpdateOrDelete){
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!

            update(subscriberToUpdateOrDelete)
        }
        else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscribers(0, name, email))
            inputName.value = null
            inputEmail.value = null
        }
    }
    fun clearOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }
        else{
            clearAll()
        }
    }
    fun insert(subscriber: Subscribers){
        viewModelScope.launch {
            repository.insert(subscriber)
            statusMessage.value = Event("Subscriber inserted successfully")
        }
    }
    private fun update(subscriber: Subscribers){
        viewModelScope.launch {
            repository.update(subscriber)
            inputName.value  = null
            inputEmail.value  = null
            isUpdateOrDelete = false
            saveOrUpdateButton.value = "Save"
            clearOrDeleteButton.value = "Clear All"
            statusMessage.value = Event("Subscriber updated successfully")
        }
    }
    private fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
        inputName.value = null
        inputEmail.value = null

        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear all"
        statusMessage.value = Event("All Subscriber deleted successfully")
    }
    private fun delete(subscriber: Subscribers) = viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value  = null
        inputEmail.value  = null
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear All"
        statusMessage.value = Event("Subscriber deleted successfully")
    }

    fun initUpdateAndDelete(subscriber: Subscribers){
        inputName.value  = subscriber.name
        inputEmail.value  = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButton.value = "Update"
        clearOrDeleteButton.value = "Delete"
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}