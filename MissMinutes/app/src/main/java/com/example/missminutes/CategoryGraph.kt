package com.example.missminutes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class CategoryGraph : AppCompatActivity() {

    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    private lateinit var categoryArrayList: ArrayList<CategorySummary>
   // private lateinit var categoryHoursMap: HashMap<String, Double>
    private lateinit var myCategoryAdapter: MyCategoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var lineChart: LineChart



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_graph)



        //THis method retrieves the data from firesotre

        //an instance of the firestore is created.
        db = FirebaseFirestore.getInstance()
        //The user id is retrieved and stored in a variable.
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid
//The collection that is being queried is the tasks collection where the userr id is equal to the cureent user's id.
        //This makes it so that only the categories created by this current user is retrieved.
        db.collection("tasks").whereEqualTo("userId", userUUID)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    var x=0
                    val entries = mutableListOf<Entry>()

//This is a foreach loop that handles the retrieving of info, and handles errors in case of any values being null or empty.
                    value?.documentChanges?.forEach { dc ->
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val task = dc.document.toObject(Task::class.java)

                            val category =
                                task.categoryName ?: "Unknown" // Default to "Unknown" if null
                            val hours = task.hours?.toDouble() ?: 0.0 // Default to 0.0 if null
                            entries.add(Entry(x.toFloat(),hours.toFloat()))
                            // Accumulate hours for each category
                           // categoryHoursMap[category] =
                          //      categoryHoursMap.getOrDefault(category, 0.0) + hours
                        }
                    }

                    // Create a list of unique categories with their total hours
                    // val uniqueCategoriesList = categoryHoursMap.map { (categoryName, totalHours) ->
                    // CategorySummary(categoryName, totalHours).categoryName
                    //}.toMutableList()

                   // val categoryNames = categoryHoursMap.map { (categoryName, totalHours) ->
                   //     CategorySummary(categoryName, totalHours).categoryName
                  //  }

                   // val totalHours = categoryHoursMap.map { (categoryName, totalHours) ->
                   //     CategorySummary(categoryName, totalHours).totalHours.toFloat()
                    //}




                    //while(x < categoryHoursMap.count()){
                    //entries.add(Entry(x.toFloat(),totalHours[x]))
                    //x++
                    //}

                    // Update the adapter with the list of unique categories
                    //myCategoryAdapter.updateData(uniqueCategoriesList)

                    // Update the adapter with the new data
                    //myCategoryAdapter.notifyDataSetChanged()



                    val dataSet = LineDataSet(entries, "Data")
                    //populate line chart with data
                    lineChart.data = LineData(dataSet)
                    //sets the chart on screen
                    lineChart.invalidate()



                    // on below line we are adding data
                    // to our bar entries list

                }
            })






    }


}// end c;lass