package com.example.missminutes

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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FinaliseTask : AppCompatActivity() {
    private lateinit var minGoal: EditText
    private lateinit var maxGoal: EditText
    private lateinit var FCategory: Spinner
    private lateinit var rdCategory: TextView
    private lateinit var nextFinal: Button
    private lateinit var ProfilePic: ImageView
    private lateinit var NumHours: EditText
    private lateinit var selectImageButton: Button
    private lateinit var addtofav: Button
    private var selectedImageUri: Uri? = null
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalise_task)

        rdCategory = findViewById(R.id.RedirectCategory)
        minGoal = findViewById(R.id.MinimumGoalTxtBox)
        maxGoal = findViewById(R.id.MaxGoalTxtbox)
        FCategory = findViewById(R.id.spinner)
        nextFinal = findViewById(R.id.BtnFinaliseTask)
        selectImageButton = findViewById(R.id.BtnUploadPic)
        ProfilePic = findViewById(R.id.imageView2)
        NumHours = findViewById(R.id.NumHoursTxt)
        addtofav = findViewById(R.id.BtnSave2Fav)

        storageReference = FirebaseStorage.getInstance().reference.child("task_images")

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                ProfilePic.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }

        selectImageButton.setOnClickListener {
            selectImage()
        }

        rdCategory.setOnClickListener {
            navigateToCategory()
        }

        nextFinal.setOnClickListener {
            uploadImageAndFinalizeTask()
        }

        addtofav.setOnClickListener {
            uploadImageAndSaveToCollection()
        }

        populateCategory()
    }

    private fun populateCategory() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUUID = currentUser?.uid

        db.collection("Categories").whereEqualTo("userId", userUUID).get()
            .addOnSuccessListener { documents ->
                val categoryNames = arrayListOf<String>()
                for (document in documents) {
                    val categoryName = document.getString("CategoryName")
                    if (categoryName != null) {
                        categoryNames.add(categoryName)
                    }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                FCategory.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error obtaining categories: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageAndSaveToCollection() {
        if (selectedImageUri != null) {
            val fileReference = storageReference.child("${System.currentTimeMillis()}.jpg")
            val uploadTask = fileReference.putFile(selectedImageUri!!)
            uploadTask.addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    saveToCollection(uri.toString())
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            saveToCollection("")
        }
    }

    private fun saveToCollection(imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: run {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        val minGoalValue = minGoal.text.toString()
        val maxGoalValue = maxGoal.text.toString()
        val selectedCategoryName = FCategory.selectedItem.toString()

        val taskBundle = intent.extras
        val objective = taskBundle?.getString("objective", "")
        val description = taskBundle?.getString("description", "")
        val startDate = taskBundle?.getString("startDate", "")
        val endDate = taskBundle?.getString("endDate", "")

        val taskData = hashMapOf(
            "userId" to userId,
            "objective" to objective,
            "description" to description,
            "startDate" to startDate,
            "endDate" to endDate,
            "categoryName" to selectedCategoryName,
            "minGoal" to minGoalValue,
            "maxGoal" to maxGoalValue,
            "taskImage" to imageUrl
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("Favtasks")
            .add(taskData)
            .addOnSuccessListener {
                Toast.makeText(this, "Task added to favorites!", Toast.LENGTH_SHORT).show()
                navigateBackToHomePage()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding task to favorites: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageAndFinalizeTask() {
        if (selectedImageUri != null) {
            val fileReference = storageReference.child("${System.currentTimeMillis()}.jpg")
            val uploadTask = fileReference.putFile(selectedImageUri!!)
            uploadTask.addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    finaliseTask(uri.toString())
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            finaliseTask("")
        }
    }

    private fun finaliseTask(imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: run {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        val minGoalValue = minGoal.text.toString()
        val maxGoalValue = maxGoal.text.toString()
        val numHoursString = NumHours.text.toString()
        val numHoursValue: Long = numHoursString.toLong()
        val selectedCategoryName = FCategory.selectedItem.toString()

        val taskBundle = intent.extras
        val objective = taskBundle?.getString("objective", "")
        val description = taskBundle?.getString("description", "")
        val startDate = taskBundle?.getString("startDate", "")
        val endDate = taskBundle?.getString("endDate", "")

        val taskData = hashMapOf(
            "userId" to userId,
            "objective" to objective,
            "description" to description,
            "startDate" to startDate,
            "endDate" to endDate,
            "categoryName" to selectedCategoryName,
            "minGoal" to minGoalValue,
            "maxGoal" to maxGoalValue,
            "taskImage" to imageUrl,
            "hours" to numHoursValue
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("tasks")
            .add(taskData)
            .addOnSuccessListener {
                Toast.makeText(this, "Task uploaded successfully!", Toast.LENGTH_SHORT).show()
                navigateBackToHomePage()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error finalizing task: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToCategory() {
        val intent = Intent(this, Category::class.java)
        startActivity(intent)
    }

    private fun selectImage() {
        getContent.launch("image/*")
    }

    private fun navigateBackToHomePage() {
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "FinaliseTask"
    }
}
