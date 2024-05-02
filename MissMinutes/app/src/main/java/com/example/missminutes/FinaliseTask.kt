package com.example.missminutes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.*
import androidx.activity.enableEdgeToEdge

class FinaliseTask : AppCompatActivity() {
    private lateinit var minGoal: EditText
    private lateinit var maxGoal: EditText
    private lateinit var FCategory: Spinner
    private lateinit var rdCategory: TextView
    private lateinit var nextFinal: Button
    private lateinit var ProfilePic: ImageView
    private lateinit var selectImageButton: Button
    private var selectedImageUri: Uri? = null
    private lateinit var getContent: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalise_task)
        rdCategory = findViewById(R.id.RedirectCategory)
        minGoal = findViewById(R.id.MinimumGoalTxtBox)
        maxGoal = findViewById(R.id.MaxGoalTxtbox)
        FCategory = findViewById(R.id.spinner)
        nextFinal = findViewById(R.id.BtnFinaliseTask)
        selectImageButton = findViewById(R.id.BtnUploadPic)
        ProfilePic = findViewById(R.id.imageView2)
// Initialize Activity Result Launcher
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                ProfilePic.setImageURI(selectedImageUri)
            }
        }
        selectImageButton.setOnClickListener {
            selectImage()
        }
        rdCategory.setOnClickListener {
            navigateToCategory()
        }
        nextFinal.setOnClickListener {
            Final()
        }
        populateCategory()
    }
    private fun populateCategory() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid // Get the current user's ID
        val db = FirebaseFirestore.getInstance()
        db.collection("Categories").get()
            .addOnSuccessListener { documents ->
                val categoryNames = mutableListOf<String>()
                for (document in documents) {
                    val categoryName = document.getString("CategoryName")
                    categoryName?.let { categoryNames.add(it) }
                }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                FCategory.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error obtaining Categories: ${exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    private fun Final() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid // Get the current user's ID
        val minGoalValue = minGoal.text.toString()
        val maxGoalValue = maxGoal.text.toString()
        val selectedCategoryName = FCategory.selectedItem.toString() // Get the selected category name from the spinner
// Get bundle data
        val taskBundle = intent.extras
        val objective = taskBundle?.getString("objective", "")
        val description = taskBundle?.getString("description", "")
        val startDate = taskBundle?.getString("startDate", "")
        val endDate = taskBundle?.getString("endDate", "")
        val image = selectedImageUri ?: Uri.EMPTY
// Store the min and max goals along with the selected category name in Firestore
        val db = FirebaseFirestore.getInstance()
        val taskData = hashMapOf(
            "userId" to userId,
            "objective" to objective,
            "description" to description,
            "startDate" to startDate,
            "endDate" to endDate,
            "categoryName" to selectedCategoryName,
            "minGoal" to minGoalValue,
            "maxGoal" to maxGoalValue,
            "taskImage" to image.toString() // Convert Uri to String for storage
        )
        db.collection("tasks")
            .add(taskData)
            .addOnSuccessListener { documentReference ->
// Handle success
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
// Handle errors
                println("Error adding document: $e")
            }
    }
    private fun navigateToCategory() {
        val intent = Intent(this, Category::class.java)
        startActivity(intent)
    }
    private fun selectImage() {
        getContent.launch("image/*")
    }
    companion object {
        private const val TAG = "FinaliseTask"
    }
}

