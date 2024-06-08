package com.example.missminutes
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.Calendar
import java.util.Collections

class Timesheet : AppCompatActivity() {
    //The following variable, lists and design elements have been declared here and initialized at a later point.
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskArrayList : ArrayList<Task>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var favourites :ImageView
    private lateinit var filterButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet)
        //The properties of the recyclerView are set.
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        //The array is set to specific data
        taskArrayList = arrayListOf()
        //The adapter will use the info from the task array list
        myAdapter = MyAdapter(taskArrayList)
        //The recycler view adapter is set to the adapter that uses the task array list.
        recyclerView.adapter = myAdapter

        filterButton = findViewById(R.id.Filter)
        EventChangeListener()
        favourites = findViewById(R.id.Navigate2FavouriteButton)

        //This on click listener calls the method that redirects the user to favourite timesheet page
        favourites.setOnClickListener {
            navigateToFavourites()
        }

        //This on click listener calls the method that opens the date picker.
        filterButton.setOnClickListener {
            openDatePicker()
        }
    }
//This method opes th ate picker, sets the parameters for the date picker
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                filterByStartDate(selectedDate)  // Filter by the selected date
            },
            year,
            month,
            day
        )


        datePickerDialog.show()  // Display the dialog
    }

    // Filter tasks by start date
    private fun filterByStartDate(selectedDate: String) {
        val filteredTasks = taskArrayList.filter { task ->
            task.startDate == selectedDate  // Check if the task's start date matches
        }

        myAdapter.updateData(filteredTasks)  // Update the adapter with filtered data
    }



    override fun onBackPressed() {
        // Navigate to HomePage when back button is pressed
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

        // Call super to ensure default back behavior is executed
        super.onBackPressed()
    }
//This method redirects the user to the favourite timesheets page
    private fun navigateToFavourites() {
        val intent = Intent(this, favouriteTimesheet::class.java)
        startActivity(intent)
        finish()
    }
    private fun EventChangeListener() {
        //An instance of firebase firesotre is created
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid/// Get the current user's ID

        //The collection called tasks is queried for the tasks that include the current user's user id.
        //THe data that is found is added to the task array list.
        db.collection("tasks").whereEqualTo("userId", userUUID)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {

                    }
                    for (dc: DocumentChange in value?.documentChanges!!)
                    {
                        if (dc.type == DocumentChange.Type.ADDED){
                            taskArrayList.add(dc.document.toObject(Task::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}