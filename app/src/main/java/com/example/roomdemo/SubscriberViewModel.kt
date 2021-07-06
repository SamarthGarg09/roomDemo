package com.example.roomdemo


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.SubscriberRepository
import com.example.roomdemo.db.Subscribers
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository:SubscriberRepository):ViewModel(), Observable {

    val subscribers = repository.subscriber
    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButton = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteButton = MutableLiveData<String>()

    init{
        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear All"
    }
    fun saveOrUpdate(){

            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscribers(0, name, email))
            inputName.value = null
            inputEmail.value = null

    }
    fun clearOrDelete(){
        clearAll()
    }
    fun insert(subscriber: Subscribers){
        viewModelScope.launch {
            repository.insert(subscriber)
        }
    }
    private fun update(subscriber: Subscribers){
        viewModelScope.launch {
            repository.update(subscriber)
        }
    }
    private fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
        inputName.value = null
        inputEmail.value = null

        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear all"
    }
    private fun delete(subscriber: Subscribers) = viewModelScope.launch {
        repository.delete(subscriber)

    }



    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}