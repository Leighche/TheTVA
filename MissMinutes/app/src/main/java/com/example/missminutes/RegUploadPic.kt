package com.example.missminutes
import android.accounts.Account
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegUploadPic : AppCompatActivity() {
    private lateinit var btnImg1: Button
    private lateinit var btnImg2: Button
    private lateinit var btnImg3: Button
    private lateinit var btnImg4: Button
    private lateinit var btnOwn: TextView
    private var selectedImageUri: Uri? = null
    private lateinit var createAccount: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_upload_pic)
//have on click listener
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid // Get the current user's ID
//This is the first button
        btnImg1.setOnClickListener {
            val image = R.drawable.img
//The data is stored in a hashmap
            val userInfo = hashMapOf(
                "userId" to userId,
                "profilePic" to image
            )
// Store the userID and profile pic in Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .add(userInfo)
                .addOnSuccessListener { documentReference ->
// Handle success
                    println("DocumentSnapshot added with ID: ${documentReference.id}")

                }
                .addOnFailureListener { e ->
// Handle errors
                    println("Error adding document: $e")
                }
        }



//This is the second button
        btnImg2.setOnClickListener {
//The data is stored in a hashmap
            val image = R.drawable.img_1
            val userInfo = hashMapOf(
                "userId" to userId,
                "profilePic" to image
            )
// Store the data in Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .add(userInfo)
                .addOnSuccessListener { documentReference ->
// Handle success
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
// Handle errors
                    println("Error adding document: $e")
                }
        }
//This is the third button.
        btnImg3.setOnClickListener {
//The data is stored in a hashmap
            val image = R.drawable.img_2
            val userInfo = hashMapOf(
                "userId" to userId,
                "profilePic" to image
            )
// Store the uuid and profile pic Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .add(userInfo)
                .addOnSuccessListener { documentReference ->
// Handle success
                    println("DocumentSnapshot added with ID: ${documentReference.id}")

                }
                .addOnFailureListener { e ->
// Handle errors
                    println("Error adding document: $e")
                }
        }
//This is the 4th button.
        btnImg4.setOnClickListener {
            val image = R.drawable.img_3
//The data is stored in a hashmap
            val userInfo = hashMapOf(
                "userId" to userId,
                "profilePic" to image
            )
// Store the user id and the profile picture ion Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .add(userInfo)
                .addOnSuccessListener { documentReference ->
// Handle success
                    println("DocumentSnapshot added with ID: ${documentReference.id}")

                }
                .addOnFailureListener { e ->
// Handle errors
                    println("Error adding document: $e")
                }
        }
//This is the button for where the user uploads their own picture.
        btnOwn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            val image = selectedImageUri ?: Uri.EMPTY
            val userInfo = hashMapOf(
                "userId" to userId,
                "profilePic" to image
            )
// Store the user id and the profile picture ion Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .add(userInfo)
                .addOnSuccessListener { documentReference ->
// Handle success
                    println("DocumentSnapshot added with ID: ${documentReference.id}")

                }
                .addOnFailureListener { e ->
// Handle errors
                    println("Error adding document: $e")
                }
        }

        createAccount = findViewById(R.id.btnCreateAccount)

        createAccount.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RegUploadPic.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
        }
    }
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val TAG = "RegUploadPic"
    }
}