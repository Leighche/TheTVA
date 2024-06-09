package com.example.missminutes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase

class Category : AppCompatActivity() {
    // Variables for UI elements
    private lateinit var categoryNameEditText: EditText
    private lateinit var categoryDescriptionEditText: EditText
    private lateinit var addCategoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Initialize UI elements
        categoryNameEditText = findViewById(R.id.CategoryNameTxt)
        categoryDescriptionEditText = findViewById(R.id.CategoryDescriptionTxt)
        addCategoryButton = findViewById(R.id.BtnAddCategory)

        // Set click listener for add category button
        addCategoryButton.setOnClickListener {
            if (validateInputs()) {
                addCategory()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        super.onBackPressed()
    }

    private fun validateInputs(): Boolean {
        val categoryName = categoryNameEditText.text.toString().trim()
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Category Name cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addCategory() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        val categoryName = categoryNameEditText.text.toString().trim()
        val categoryDescription = categoryDescriptionEditText.text.toString().trim()

        val db = FirebaseFirestore.getInstance()
        val categoryData = hashMapOf(
            "userId" to userId,
            "CategoryName" to categoryName,
            "CategoryDescription" to categoryDescription
        )

        db.collection("Categories")
            .add(categoryData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()
                saveToRealtimeDatabase(userId, categoryName, categoryDescription)
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding category", e)
                Toast.makeText(this, "Error adding category: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToRealtimeDatabase(userId: String, categoryName: String, categoryDescription: String) {
        val database = FirebaseDatabase.getInstance().reference
        val categoryData = mapOf(
            "userId" to userId,
            "CategoryName" to categoryName,
            "CategoryDescription" to categoryDescription
        )

        // Using push() to generate a unique key for the new category
        val newCategoryRef = database.child("Categories").push()

        newCategoryRef.setValue(categoryData)
            .addOnSuccessListener {
                Log.d("RealtimeDatabase", "Category added to Realtime Database successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("RealtimeDatabase", "Error adding category to Realtime Database", e)
            }
    }
}
