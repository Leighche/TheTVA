package com.example.missminutes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FinaliseTask : AppCompatActivity() {
    //The following variables are assigned to some value from the design elements.
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalise_task)
//The values that are stored in the design elements are stored in the variables now.
        rdCategory = findViewById(R.id.RedirectCategory)
        minGoal = findViewById(R.id.MinimumGoalTxtBox)
        maxGoal = findViewById(R.id.MaxGoalTxtbox)
        FCategory = findViewById(R.id.spinner)
        nextFinal = findViewById(R.id.BtnFinaliseTask)
        selectImageButton = findViewById(R.id.BtnUploadPic)
        ProfilePic = findViewById(R.id.imageView2)
        NumHours =findViewById(R.id.NumHoursTxt)
        addtofav = findViewById(R.id.BtnSave2Fav)

        // Initialize Activity Result Launcher
        //This saves an image in a variable and assigns it to an imageView
        //IF there is no image that has been uploaded then there will be a pop up box notifying the user of this.
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                ProfilePic.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }
//This on click listener will trigger the selectImage() method.
        selectImageButton.setOnClickListener {
            selectImage()
        }

        //This on click listener will trigger the navigateToCategory() method.
        rdCategory.setOnClickListener {
            navigateToCategory()
        }

        //This on click listener will trigger the finalizeTask() method.
        nextFinal.setOnClickListener {
            finaliseTask()
        }
//This on click listener will trigger the saveToCollection() method.

        addtofav.setOnClickListener {
            saveToCollection()
        }
//This calls the populateCategory() method.
        populateCategory()
    }
//This method populates the category combo box.
    private fun populateCategory() {
        //An instance of the firestore database is retrieved.
        val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userUUID = currentUser?.uid/// Get the current user's ID

    //The collection that is being called is categories
        db.collection("Categories").whereEqualTo("userId", userUUID).get()
            .addOnSuccessListener { documents ->
                val categoryNames = arrayListOf<String>()
                //The cateogries are added to the arrayList
                for (document in documents) {
                    val categoryName = document.getString("CategoryName")
                    if (categoryName != null) {
                        categoryNames.add(categoryName)
                    }
                }
                //The adapter properties is set.
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                FCategory.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error obtaining categories: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
//This method sends the data to firestore as a favourite task.
    private fun saveToCollection() {
        
        //The current user id is stored in a variable.
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: run {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }
//The data from the design elements is saved in variables
        val minGoalValue = minGoal.text.toString()
        val maxGoalValue = maxGoal.text.toString()
        val selectedCategoryName = FCategory.selectedItem.toString()

    //The data from the previous activity is retrieved from the bundle.
        val taskBundle = intent.extras
        val objective = taskBundle?.getString("objective", "")
        val description = taskBundle?.getString("description", "")
        val startDate = taskBundle?.getString("startDate", "")
        val endDate = taskBundle?.getString("endDate", "")
        val image = selectedImageUri ?: Uri.EMPTY
//The data is placed in a hashMap
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
//An instance of firestore is retrieved
        val db = FirebaseFirestore.getInstance()
//The follwoing addds the task to the favtasks collection if the user wishes to do so.
        db.collection("Favtasks")
            .add(taskData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Task added to favorites!", Toast.LENGTH_SHORT).show()

                navigateBackToHomePage() // Navigate to HomePage and close current activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error adding task to favorites: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
//This method saves the data to a collection called tasks
    private fun finaliseTask() {
        //the current user's id is retrieved.
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: run {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }
//the data from the  activity is stored in the variables
        val minGoalValue = minGoal.text.toString()
        val maxGoalValue = maxGoal.text.toString()
        val numHoursString = NumHours.text.toString()
        val numHoursValue: Long = numHoursString.toLong()
        val selectedCategoryName = FCategory.selectedItem.toString()

    //the data from the previous activity is retrived through a bundle
        val taskBundle = intent.extras
        val objective = taskBundle?.getString("objective", "")
        val description = taskBundle?.getString("description", "")
        val startDate = taskBundle?.getString("startDate", "")
        val endDate = taskBundle?.getString("endDate", "")
        val image = selectedImageUri ?: Uri.EMPTY
//the data is put into a hashmap
        val taskData = hashMapOf(
            "userId" to userId,
            "objective" to objective,
            "description" to description,
            "startDate" to startDate,
            "endDate" to endDate,
            "categoryName" to selectedCategoryName,
            "minGoal" to minGoalValue,
            "maxGoal" to maxGoalValue,
            "taskImage" to image.toString(),
            "hours" to numHoursValue
        )
//an instance of the firestore db is retrieved
        val db = FirebaseFirestore.getInstance()
//the collection that the task is added to is called tasks
        db.collection("tasks")
            .add(taskData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    this,
                    "Task uploaded successfully!",
                    Toast.LENGTH_SHORT
                ).show()
// Navigate to HomePage and close current activity
                navigateBackToHomePage()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error finalising task: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
//This method redirects the user to the add cateogry page
    private fun navigateToCategory() {
        val intent = Intent(this, Category::class.java)
        startActivity(intent)
    }
//This method allows the user to select an image
    private fun selectImage() {
        getContent.launch("image/*")
    }

    //This method redirects to the home page.
    private fun navigateBackToHomePage() {
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

        // Close current activity
        finish()
    }

    companion object {
        private const val TAG = "FinaliseTask"
    }
}
