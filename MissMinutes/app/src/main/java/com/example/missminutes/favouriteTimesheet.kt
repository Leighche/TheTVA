package com.example.missminutes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*

class favouriteTimesheet : AppCompatActivity() {
//The following variables are assigned some data from either the design of teh activity or from firestore.
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskArrayList: ArrayList<Task>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_timesheet)
//The properties of the recycler view are set here
        recyclerView = findViewById(R.id.favRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Initialize the task list and adapter
        taskArrayList = arrayListOf()
        myAdapter = MyAdapter(taskArrayList)
        recyclerView.adapter = myAdapter
// Start listening for Firestore changes
        EventChangeListener()
    }

    override fun onBackPressed() {
        // Navigate to HomePage when back button is pressed
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

        // Call super to ensure default back behavior is executed
        super.onBackPressed()
    }

    private fun EventChangeListener() {
        //The current user's user id is retrieved and stored.
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid  // Get the current user's ID
//IF the user is null then the error message is logged.
        if (userUUID == null) {
            Log.e("favouriteTimesheet", "User ID is null. Can't query Firestore without a valid user.")
            return  // Exit early if there's no user ID
        }
//The collection that will be accessed is Favtasks and it will be filtered by the current user's id.
        //There is also error handling in place
        db.collection("Favtasks").whereEqualTo("userId", userUUID)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return@addSnapshotListener  // Exit if there's an error
                }

                if (value == null) {
                    Log.w("favouriteTimesheet", "QuerySnapshot returned null. No tasks to display.")
                    return@addSnapshotListener
                }

                taskArrayList.clear()  // Clear existing tasks to avoid duplicates
                //The taskss are added to an arraylist so that it can be displayed by to user.
                for (dc in value.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val task = dc.document.toObject(Task::class.java)
                        taskArrayList.add(task)  // Add the new task
                    }
                }

                myAdapter.notifyDataSetChanged()  // Update the adapter
            }
    }
}
