package com.example.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ListItemBinding
import com.example.roomdemo.db.Subscribers


class MyRecyclerViewAdapter(private val subscribersList : List<Subscribers>,private val clickListener:(Subscribers)->Unit) :RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater,R.layout.list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(subscribersList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return subscribersList.size
    }

}
class MyViewHolder( private val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root)
{
    fun bind(subscribers: Subscribers,clickListener: (Subscribers) -> Unit){
        binding.nameTextView.text = subscribers.name
        binding.emailTextView.text = subscribers.email
        binding.listItemLayout.setOnClickListener {
            clickListener(subscribers)
        }
    }
}