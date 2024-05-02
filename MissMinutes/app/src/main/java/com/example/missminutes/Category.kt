package com.example.missminutes

<<<<<<< Updated upstream
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Category : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
=======
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Category : AppCompatActivity() {

    private lateinit var CategoryName: EditText
    private lateinit var CategoryDescription: EditText
    private  lateinit var AddCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        CategoryName = findViewById(R.id.CategoryNameTxt)
        CategoryDescription= findViewById(R.id.CategoryDescriptionTxt)
        AddCategory = findViewById(R.id.BtnAddCategory)

        AddCategory.setOnClickListener {
            NextPageFinal()
        }
    }
    private fun NextPageFinal() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid // Get the current user's ID

        val minCategoryName = CategoryName.text.toString()
        val minCategoryDescription = CategoryDescription.text.toString()

        val db = FirebaseFirestore.getInstance()
        val categoriesData = hashMapOf(
            "userId" to userId,
            "CategoryName" to minCategoryName,
            "CategoryDescription" to minCategoryDescription
        )

        db.collection("Categories")
            .add(categoriesData)
            .addOnSuccessListener { documentReference ->
                // Handle success
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                val intent = Intent(this, FinaliseTask::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // Handle errors
                println("Error adding document: $e")
            }

>>>>>>> Stashed changes
    }
}