package com.example.missminutes

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    private var categoryHoursMap: HashMap<String, Double> =HashMap<String,Double>()
    private lateinit var myCategoryAdapter: MyCategoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var lineChart: LineChart



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_graph)

        lineChart = findViewById(R.id.idLineChart)

        //THis method retrieves the data from firesotre

        //an instance of the firestore is created.
        db = FirebaseFirestore.getInstance()
        //The user id is retrieved and stored in a variable.

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid
        val dataSets = ArrayList<ILineDataSet>()

        db.collection("tasks").whereEqualTo("userId", userUUID).get().addOnSuccessListener { snapshot ->
            val entries = mutableListOf<Entry>()

            snapshot.documents.forEachIndexed{index, document ->
                try{

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
// Clear the existing data
                                categoryArrayList.clear()
                                // Clear the category-hours map
                                categoryHoursMap.clear()
//This is a foreach loop that handles the retrieving of info, and handles errors in case of any values being null or empty.
                                value?.documentChanges?.forEach { dc ->
                                    if (dc.type == DocumentChange.Type.ADDED) {
                                        val task = dc.document.toObject(Task::class.java)

                                        val category = task.categoryName ?: "Unknown" // Default to "Unknown" if null
                                        val hours = task.hours?.toDouble() ?: 0.0 // Default to 0.0 if null

                                        // Accumulate hours for each category
                                        categoryHoursMap[category] =
                                            categoryHoursMap.getOrDefault(category, 0.0) + hours
                                    }
                                }

                                // Create a list of unique categories with their total hours
                                val uniqueCategoriesList = categoryHoursMap.map { (categoryName, totalHours) ->
                                    CategorySummary(categoryName, totalHours)
                                }

                                // Update the adapter with the list of unique categories
                                myCategoryAdapter.updateData(uniqueCategoriesList)

                                // Update the adapter with the new data
                                myCategoryAdapter.notifyDataSetChanged()
                            }
                        })

                    val yValue = document.getDouble("hours")?.toFloat()?:0f

                    entries.add(Entry(index.toFloat(),yValue))
                    val dataSet = LineDataSet(entries, "Example")
                    dataSets.add(dataSet)
                    dataSet.setDrawValues(false)
                    dataSet.setDrawFilled(true)
                    dataSet.fillColor = com.androidplot.R.color.ap_gray
                    dataSet.fillColor = androidx.appcompat.R.color.material_blue_grey_800

                }
                catch (e: RuntimeException){
                    Log.e("DataError", "Skipping doc")
                }
            }
            lineChart.setTouchEnabled(true)
            lineChart.setPinchZoom(true)

            lineChart.description.text = "Hours"
            lineChart.setNoDataText("Unavailable")

            //populate line chart with data
            lineChart.data = LineData(dataSets)
            //sets the chart on screen
            lineChart.invalidate()
        }










//while(x < categoryHoursMap.count()){
        //entries.add(Entry(x.toFloat(),totalHours[x]))
        //x++
        //}

        // Update the adapter with the list of unique categories
        //myCategoryAdapter.updateData(uniqueCategoriesList)


//The collection that is being queried is the tasks collection where the userr id is equal to the cureent user's id.
//This makes it so that only the categories created by this current user is retrieved.
//db.collection("tasks").whereEqualTo("userId", userUUID)
//.addSnapshotListener(object : EventListener<QuerySnapshot> {
        //  override fun onEvent(
        //  value: QuerySnapshot?,
        // error: FirebaseFirestoreException?
        // ) {
        //  if (error != null) {
        //    Log.e("Firestore error", error.message.toString())
        // return
        //  }

        // var x=0
        //val entries = mutableListOf<Entry>()

//This is a foreach loop that handles the retrieving of info, and handles errors in case of any values being null or empty.
        // value?.documentChanges?.forEach { dc ->
        //  if (dc.type == DocumentChange.Type.ADDED) {
        //   val task = dc.document.toObject(Task::class.java)

        // val category =
        //     task.categoryName ?: "Unknown" // Default to "Unknown" if null
        // val hours = task.hours?.toDouble() ?: 0.0 // Default to 0.0 if null
        //  entries.add(Entry(x.toFloat(),hours.toFloat()))
        // Accumulate hours for each category
        // categoryHoursMap[category] =
        //      categoryHoursMap.getOrDefault(category, 0.0) + hours
        //  }
        // }

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



    }


}// end c;lass