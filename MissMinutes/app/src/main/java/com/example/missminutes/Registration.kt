package com.example.missminutes
<<<<<<< Updated upstream
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
=======

import android.content.Intent
import android.os.Bundle
import android.util.Log
>>>>>>> Stashed changes
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
<<<<<<< Updated upstream
import com.google.firebase.FirebaseApp
=======
import androidx.appcompat.app.AppCompatActivity
>>>>>>> Stashed changes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registration : AppCompatActivity() {

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
        Email = findViewById(R.id.RegUser)
        Password = findViewById(R.id.RegPass)
        ConfirmPassword = findViewById(R.id.RegConfirmPass)
        btnSignUp = findViewById(R.id.btnSignUp)
        LoginPageLink = findViewById(R.id.Redirect2Login)
        auth = Firebase.auth

<<<<<<< Updated upstream

        // Initializing Firebase auth object
        auth = FirebaseAuth.getInstance()

=======
>>>>>>> Stashed changes
        btnSignUp.setOnClickListener {
            signUp()
        }

        LoginPageLink.setOnClickListener {
<<<<<<< Updated upstream
            navigateToLogin()
=======
            navigateToLogin()  // Ensure this only navigates to login when intended
>>>>>>> Stashed changes
        }
    }

    private fun signUp() {
        val email = Email.text.toString()
        val password = Password.text.toString()

<<<<<<< Updated upstream
        // calling createUserWithEmailAndPassword(email, password)
        // function using Firebase auth object
        // On successful response Display a Toast
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                // You can add additional actions here after successful registration
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
=======
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != ConfirmPassword.text.toString()) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication for creating a new user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Log.d("Registration", "Successfully registered, navigating to RegUploadPic")
                navigateToRegUpload()
            } else {
                Toast.makeText(this, "Registration failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
>>>>>>> Stashed changes
            }
        }
    }

<<<<<<< Updated upstream
    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
=======
    private fun navigateToRegUpload() {
        Log.d("Registration", "Navigating to RegUploadPic")
        val intent = Intent(this, RegUploadPic::class.java)
        startActivity(intent)
        finish()  // Ensure the current activity is closed after navigation
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Ensure the current activity is closed after navigation
    }
}
>>>>>>> Stashed changes
