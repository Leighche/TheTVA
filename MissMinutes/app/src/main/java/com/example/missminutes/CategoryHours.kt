package com.example.missminutes

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CategoryHours : AppCompatActivity() {
    //The following variables are assigned to design elements from this activity.
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryArrayList: ArrayList<CategorySummary>
    private lateinit var categoryHoursMap: HashMap<String, Double>
    private lateinit var myCategoryAdapter: MyCategoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var buttonOpenDatePicker: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_hours)
//The follwoing variables are string the data from the design elements
        recyclerView = findViewById(R.id.recyclerView)

        //The properties of the recyclerView are set here.
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        //Initialize the catoegory array list
        categoryArrayList = arrayListOf()
        // Initialize the category hours map
        categoryHoursMap = hashMapOf()
        //The cateogry array list is assigned to the adapter.
        myCategoryAdapter = MyCategoryAdapter(categoryArrayList)
        //the recyclerView's adapter now containts the adapter above.
        recyclerView.adapter = myCategoryAdapter

        //The following variables sotre the user's input.
        buttonOpenDatePicker = findViewById(R.id.filterByDate)
        buttonOpenDatePicker.setOnClickListener { openDatePicker() }

        //Method call to Fetch tasks from Firestore
        fetchTasks()

        val graph = findViewById<Button>(R.id.btnGraph)
        graph.setOnClickListener { navigateToCategory() }

    }

    private fun navigateToCategory() {
        startActivity(Intent(this, CategoryGraph::class.java))
        finish()
    }

    //This method handles the user clicking the back button
    override fun onBackPressed() {
        // Navigate to HomePage when back button is pressed
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

        // Call super to ensure default back behavior is executed
        super.onBackPressed()
    }
//THis method retrieves the data from firesotre
    private fun fetchTasks() {
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
    }

//This method opens the date picker for the user to choose the date that they want to filter by.
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/${selectedYear}"
                buttonOpenDatePicker.text = selectedDate
                filterTasksByDate(selectedDate) // Implement filtering by date
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
//This method filters the task by date.
private fun filterTasksByDate(selectedDate: String) {
    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.US)
    val selectedDateObject = dateFormat.parse(selectedDate)

    db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userUUID = currentUser?.uid

    db.collection("tasks")
        .whereEqualTo("userId", userUUID)
        .whereEqualTo("date", selectedDateObject)
        .get()
        .addOnSuccessListener { querySnapshot ->
            categoryArrayList.clear()
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

            myCategoryAdapter.updateData(uniqueCategoriesList)
            myCategoryAdapter.notifyDataSetChanged() // Refresh the RecyclerView with the filtered data
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore Error", "Error fetching tasks by date: ${exception.message}")
        }
}
}
