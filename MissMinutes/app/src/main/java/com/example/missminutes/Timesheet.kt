package com.example.missminutes
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
class Timesheet : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskArrayList : ArrayList<Task>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var favourites :ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        taskArrayList = arrayListOf()
        myAdapter = MyAdapter(taskArrayList)
        recyclerView.adapter = myAdapter
        EventChangeListener()
        favourites = findViewById(R.id.Navigate2FavouriteButton)
        favourites.setOnClickListener {
            navigateToFavourites()
        }
    }

    private fun navigateToFavourites() {
        val intent = Intent(this, favouriteTimesheet::class.java)
        startActivity(intent)
        finish()
    }
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid/// Get the current user's ID
        db.collection("tasks").whereEqualTo("userId", userUUID)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
// Log.e("Firestore error", error.message.toString())
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