package com.example.missminutes
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
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


        // Initializing Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            signUp()
            val intent = Intent(this, RegUploadPic::class.java)
            startActivity(intent)
        }

        LoginPageLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun signUp() {
        val email = Email.text.toString()
        val password = Password.text.toString()

        // calling createUserWithEmailAndPassword(email, password)
        // function using Firebase auth object
        // On successful response Display a Toast
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                // You can add additional actions here after successful registration
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}