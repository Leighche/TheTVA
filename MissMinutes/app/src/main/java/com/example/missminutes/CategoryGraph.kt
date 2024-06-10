package com.example.missminutes
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.play.integrity.internal.al
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CategoryGraph : AppCompatActivity() {
    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    //tan@gmai.com private lateinit var categoryArrayList: ArrayList<CategorySummary>
    private var categoryHoursMap: HashMap<String, Double> =HashMap<String,Double>()
    private lateinit var myCategoryAdapter: MyCategoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var lineChart: LineChart
    private lateinit var buttonOpenStartDatePicker: Button
    private lateinit var buttonOpenEndDatePicker: Button
    private lateinit var buttonFilter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_graph)
        lineChart = findViewById(R.id.idLineChart)
        db = FirebaseFirestore.getInstance()


        buttonOpenStartDatePicker = findViewById(R.id.filterByStartDate)
        buttonOpenStartDatePicker.setOnClickListener { openStartDatePicker() }
        buttonOpenEndDatePicker = findViewById(R.id.filterByEndDate)
        buttonOpenEndDatePicker.setOnClickListener { openEndDatePicker() }
        buttonFilter = findViewById(R.id.btnFilter)
        buttonFilter.setOnClickListener {filterData()}
        fetchData()
    }

    private fun filterData() {

        var selectedStartDate = buttonOpenStartDatePicker.text.toString()
        var selectedEndDate = buttonOpenEndDatePicker.text.toString()

        val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.US)
        val selectedStartDateObject = dateFormat.parse(selectedStartDate)
        val selectedEndDateObject = dateFormat.parse(selectedEndDate)


        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid

        db.collection("tasks")
            .whereEqualTo("userId", userUUID)
            //.whereEqualTo("date", selectedStartDateObject)
            .whereGreaterThanOrEqualTo("date", selectedStartDateObject)
            .whereLessThanOrEqualTo("date",selectedEndDateObject)
            .get()
            .addOnSuccessListener { querySnapshot ->

                categoryHoursMap.clear()

                // Loop through tasks and group them by category
                querySnapshot.forEach { document ->
                    val task = document.toObject(Task::class.java)
                    val category = task.categoryName ?: "Unknown"
                    val hours = task.hours?.toDouble() ?: 0.0

                    // Accumulate hours for each category
                    categoryHoursMap[category] = categoryHoursMap.getOrDefault(category, 0.0) + hours
                }

                // Create a list of unique categories with their total hours
                val uniqueCategoriesList = categoryHoursMap.map { (categoryName, totalHours) ->
                    CategorySummary(categoryName, totalHours)
                }

                val entries = uniqueCategoriesList.mapIndexed { index, categorySummary ->
                    Entry(index.toFloat(), categorySummary.totalHours.toFloat())
                }
                val dataSet = LineDataSet(entries, "Total Hours")
                val lineData = LineData(dataSet)
                lineChart.setTouchEnabled(true)
                lineChart.setPinchZoom(true)
                lineChart.data = lineData
                lineChart.description.text = "Total Hours Per Category"
                val categories = uniqueCategoriesList.map { it.categoryName }
                lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(categories)
                lineChart.xAxis.granularity = 1f
// lineChart.xAxis.granularity.e
                lineChart.notifyDataSetChanged();
                lineChart.invalidate() // Refresh the chart

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore Error", "Error fetching tasks by date: ${exception.message}")
            }
    }

    private fun openEndDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/${selectedYear}"
                buttonOpenEndDatePicker.text = selectedDate
                //filterTasksByDate(selectedDate) // Implement filtering by date
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun openStartDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/${selectedYear}"
                buttonOpenStartDatePicker.text = selectedDate
                //filterTasksByDate(selectedDate) // Implement filtering by date

            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun fetchData() {
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
// Clear the existing data
// categoryHoursMap.clear()
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

                    displayChart(uniqueCategoriesList)
                }
            })
    }
    private fun displayChart(categorySummaries: List<CategorySummary>) {
        val entries = categorySummaries.mapIndexed { index, categorySummary ->
            Entry(index.toFloat(), categorySummary.totalHours.toFloat())
        }
        val dataSet = LineDataSet(entries, "Total Hours")
        val lineData = LineData(dataSet)
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.data = lineData
        lineChart.description.text = "Total Hours Per Category"
        val categories = categorySummaries.map { it.categoryName }
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(categories)
        lineChart.xAxis.granularity = 1f
// lineChart.xAxis.granularity.e
 lineChart.notifyDataSetChanged();
        lineChart.invalidate() // Refresh the chart
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
// override fun onEvent(
// value: QuerySnapshot?,
// error: FirebaseFirestoreException?
// ) {
// if (error != null) {
// Log.e("Firestore error", error.message.toString())
// return
// }
// var x=0
//val entries = mutableListOf<Entry>()
//This is a foreach loop that handles the retrieving of info, and handles errors in case of any values being null or empty.
// value?.documentChanges?.forEach { dc ->
// if (dc.type == DocumentChange.Type.ADDED) {
// val task = dc.document.toObject(Task::class.java)
// val category =
// task.categoryName ?: "Unknown" // Default to "Unknown" if null
// val hours = task.hours?.toDouble() ?: 0.0 // Default to 0.0 if null
// entries.add(Entry(x.toFloat(),hours.toFloat()))
// Accumulate hours for each category
// categoryHoursMap[category] =
// categoryHoursMap.getOrDefault(category, 0.0) + hours
// }
// }
// Create a list of unique categories with their total hours
// val uniqueCategoriesList = categoryHoursMap.map { (categoryName, totalHours) ->
// CategorySummary(categoryName, totalHours).categoryName
//}.toMutableList()
// val categoryNames = categoryHoursMap.map { (categoryName, totalHours) ->
// CategorySummary(categoryName, totalHours).categoryName
// }
// val totalHours = categoryHoursMap.map { (categoryName, totalHours) ->
// CategorySummary(categoryName, totalHours).totalHours.toFloat()
//}



}// end c;lass