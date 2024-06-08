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
import com.google.firebase.firestore.FirebaseFirestoreException

class Category : AppCompatActivity() {
    //These variables are assigned to the design elements for this activity

    private lateinit var categoryNameEditText: EditText
    private lateinit var categoryDescriptionEditText: EditText
    private lateinit var addCategoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
//These variables are going to sotre the info that these design elements are storing.
        categoryNameEditText = findViewById(R.id.CategoryNameTxt)
        categoryDescriptionEditText = findViewById(R.id.CategoryDescriptionTxt)
        addCategoryButton = findViewById(R.id.BtnAddCategory)


        //THis is an on-click listener for the add category button
        addCategoryButton.setOnClickListener {
            //This is error handling and if all the input is in the correct form then the method is called
            if (validateInputs()) {
                addCategory()
            }
        }
    }
//This method handles the user pressing the 'Back' button
    override fun onBackPressed() {
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        super.onBackPressed()
    }

    //This methods handles error handling.
    private fun validateInputs(): Boolean {
        val categoryName = categoryNameEditText.text.toString().trim()
        //This if statement validates the data to not be empty
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Category Name cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        //if the data is validatedm, then the method will return true.
        return true
    }
//THis method adds the category to the firestore.
    private fun addCategory() {
        //The user id is accessed here.
        val currentUser = FirebaseAuth.getInstance().currentUser
    //If the id is null then a pop up will show saying that the user has not been authenticated.
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }
        //The user id value is assigned to the val variable called 'userId'
        val userId = currentUser.uid
    //The name and description of the category is saved to the follow variables
        val categoryName = categoryNameEditText.text.toString().trim()
        val categoryDescription = categoryDescriptionEditText.text.toString().trim()
//An instance of the firestore is created.
        val db = FirebaseFirestore.getInstance()
    //a hashmap is created with all the data that needs to be saved to the firestore
        val categoryData = hashMapOf(
            "userId" to userId,
            "CategoryName" to categoryName,
            "CategoryDescription" to categoryDescription
        )
//This is the name of the collection that the document will be saved to .
    //the data is added and a pop up displays a message showing either success or failure with regard to storing the data.
        db.collection("Categories")
            .add(categoryData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding category", e)
                Toast.makeText(this, "Error adding category: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
