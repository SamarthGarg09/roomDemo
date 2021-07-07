package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.db.SubscriberDatabase
import com.example.roomdemo.db.SubscriberRepository
import com.example.roomdemo.db.Subscribers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel : SubscriberViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory  = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        binding.lifecycleOwner = this
        binding.myViewModel = subscriberViewModel
        initRecyclerView()

        subscriberViewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this,it,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        displaySubscribersList()
    }

     private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, {
        Log.i("MyTag",it.toString())
            binding.subscriberRecyclerView.adapter = MyRecyclerViewAdapter(it,{selectedListItem->listItemClicked(selectedListItem)})
        })
    }
    private fun listItemClicked(subscribers: Subscribers){
        Log.i("MyTag","subscriber name is ${subscribers.name} with email id as ${subscribers.email}")
        subscriberViewModel.initUpdateAndDelete(subscribers)
    }

}