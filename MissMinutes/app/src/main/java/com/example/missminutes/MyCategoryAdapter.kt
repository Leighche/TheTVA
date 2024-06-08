package com.example.missminutes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyCategoryAdapter(private var categorySummaryList: List<CategorySummary>) :
    RecyclerView.Adapter<MyCategoryAdapter.ViewHolder>() {
//This class holds the variables that store the category name and it's total hours
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val totalHours: TextView = itemView.findViewById(R.id.totalHours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false) // Assuming a layout file for category
        return ViewHolder(itemView)
    }
//The data is displayed in the holder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categorySummary = categorySummaryList[position]
        holder.categoryName.text = categorySummary.categoryName
        holder.totalHours.text = categorySummary.totalHours.toString()
    }
//This retrieves the size of the list for the category summary
    override fun getItemCount(): Int = categorySummaryList.size

    //this method updates the list with new data.
    fun updateData(newData: List<CategorySummary>) {
        this.categorySummaryList = newData
        notifyDataSetChanged()
    }
}
