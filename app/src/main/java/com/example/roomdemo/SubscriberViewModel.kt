package com.example.roomdemo


import android.util.Patterns
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
        if(inputName.value == null){
            statusMessage.value = Event("Please enter subscribers name")
        }
        else if (inputEmail.value == null){
            statusMessage.value = Event("Please enter subscribers Email")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please enter the correct Email address")
        }
        else{
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
           val newRowId =  repository.insert(subscriber)
            if(newRowId>-1) {
                statusMessage.value = Event("Subscriber inserted successfully; Id = $newRowId")
            }else{
                statusMessage.value = Event("Error occurred")
            }
            }
    }
    private fun update(subscriber: Subscribers){
        viewModelScope.launch {
            val noOfRows = repository.update(subscriber)
            inputName.value  = null
            inputEmail.value  = null
            isUpdateOrDelete = false
            saveOrUpdateButton.value = "Save"
            clearOrDeleteButton.value = "Clear All"
            if(noOfRows>-1){
                statusMessage.value = Event("Subscriber updated successfully; Rows = $noOfRows")
            }
            else{
                statusMessage.value = Event("Error Occurred")
            }
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
        val noOfRowsDeleted = repository.delete(subscriber)
        if(noOfRowsDeleted>0){
            inputName.value  = null
            inputEmail.value  = null
            isUpdateOrDelete = false
            saveOrUpdateButton.value = "Save"
            clearOrDeleteButton.value = "Clear All"
            statusMessage.value = Event("Subscriber deleted successfully")
        }
        else{
            statusMessage.value = Event("$noOfRowsDeleted deleted successfully")
        }
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