package com.example.missminutes

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(private val taskList: ArrayList<Task>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val task : Task = taskList[position]
        holder.categoryName.text = task.categoryName
        holder.description.text = task.description
        holder.endDate.text = task.endDate
        holder.maxGoal.text = task.maxGoal
        holder.minGoal.text = task.minGoal
        holder.objective.text = task.objective
        holder.startDate.text = task.startDate

        Picasso.get().load(task.taskImage).into(holder.taskImage)

    }

    override fun getItemCount(): Int {
       return taskList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val categoryName : TextView = itemView.findViewById(R.id.categoryName)
        val description : TextView = itemView.findViewById(R.id.description)
        val endDate : TextView = itemView.findViewById(R.id.endDate)
        val maxGoal : TextView = itemView.findViewById(R.id.maxGoal)
        val minGoal : TextView = itemView.findViewById(R.id.minGoal)
        val objective : TextView = itemView.findViewById(R.id.objective)
        val startDate : TextView = itemView.findViewById(R.id.startDate)
        val taskImage : ImageView = itemView.findViewById(R.id.taskImage)
    }
}