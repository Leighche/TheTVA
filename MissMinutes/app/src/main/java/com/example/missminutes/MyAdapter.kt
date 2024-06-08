package com.example.missminutes

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso

class MyAdapter(private var taskList: MutableList<Task>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    fun updateData(newTaskList: List<Task>) {
        // Update the internal list with new data
        taskList.clear()  // Clear existing data
        taskList.addAll(newTaskList)  // Add new data
        notifyDataSetChanged()  // Refresh the UI
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        // Safely set text fields
        holder.categoryName.text = task.categoryName ?: "Unknown"
        holder.description.text = task.description ?: "No description"
        holder.endDate.text = task.endDate ?: "N/A"
        holder.maxGoal.text = task.maxGoal ?: "N/A"
        holder.minGoal.text = task.minGoal ?: "N/A"
        holder.objective.text = task.objective ?: "No objective"
        holder.startDate.text = task.startDate ?: "N/A"

        // Check if the task image path is null or empty
        if (task.taskImage.isNullOrEmpty()) {
            holder.taskImage.setImageResource(R.drawable.missminutesapplogo)  // Placeholder if no image
        } else {
            try {
                val uri = Uri.parse(task.taskImage) // Convert to URI
                Log.d("MyAdapter", "Loading image with Picasso: $uri")

                Picasso.get()
                    .load(uri) // Load the content URI with Picasso
                    .placeholder(R.drawable.missminutesapplogo) // Placeholder image
                    .error(R.drawable.missminutesapplogo) // Fallback if loading fails
                    .into(holder.taskImage) // Load into ImageView
            } catch (e: Exception) {
                Log.e("MyAdapter", "Error loading image with Picasso: ${e.message}", e) // Detailed error logging
                holder.taskImage.setImageResource(R.drawable.missminutesapplogo) // Fallback if there's an error
            }
        }
    }
//The view holder is populated
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }
//The list of task items is retrieved
    override fun getItemCount(): Int {
        return taskList.size
    }
//This clas contains variables that hold the task data
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val description: TextView = itemView.findViewById(R.id.description)
        val endDate: TextView = itemView.findViewById(R.id.endDate)
        val maxGoal: TextView = itemView.findViewById(R.id.maxGoal)
        val minGoal: TextView = itemView.findViewById(R.id.minGoal)
        val objective: TextView = itemView.findViewById(R.id.objective)
        val startDate: TextView = itemView.findViewById(R.id.startDate)
        val taskImage: ImageView = itemView.findViewById(R.id.taskImage)
    }
}