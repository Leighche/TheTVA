package com.example.missminutes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registration : AppCompatActivity() {
//These are the variables that will sotre data from the design elements of the activity
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var ConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var LoginPageLink: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // View Binding
        //The variables are amde equal to the data that user has entererd.
        Email = findViewById(R.id.RegUser)
        Password = findViewById(R.id.RegPass)
        ConfirmPassword = findViewById(R.id.RegConfirmPass)
        btnSignUp = findViewById(R.id.btnSignUp)
        LoginPageLink = findViewById(R.id.Redirect2Login)
        //The firebase authentication has been instantiated
        auth = Firebase.auth

        //This on click listener calls the emthod that signs the user up
        btnSignUp.setOnClickListener {
            signUp()
        }
//THis button on click calls the methods that redirects the user to the log in page
        LoginPageLink.setOnClickListener {
            navigateToLogin()
        }
    }
//This method signs the user up with firebase authentication
    private fun signUp() {
        //The following variables store the user's input.
        val email = Email.text.toString()
        val password = Password.text.toString()
//The following if statement is error handling to ensure that the user has filled out all teh required info
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
//The following if statement is error handling to ensure that the password edittext and the confirmPassword EditText are the same
        if (password != ConfirmPassword.text.toString()) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication for creating a new user
    //this creates a new user in firebase
    //If the user is successfully created the user the will be redirected to the homepage of the app
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Log.d("Registration", "Successfully registered, navigating to RegUploadPic")
                navigateToHomePage()
            } else {
                Toast.makeText(this, "Registration failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
//This method redirects the user to the home page
    private fun navigateToHomePage() {
        Log.d("Registration", "Navigating to RegUploadPic")
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        finish()  // Ensure the current activity is closed after navigation
    }

    //This method reidrects the user to the log in page.
    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Ensure the current activity is closed after navigation
    }
}
