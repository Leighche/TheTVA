package com.example.missminutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.window.SplashScreen
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var btnLogin: Button
    private lateinit var SignUpPageLink: TextView
    private lateinit var auth: FirebaseAuth
    private var oneTapClient: SignInClient? = null
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//Firebase is initialized
        FirebaseApp.initializeApp(this)
//These variables store the data inputs from this activity.
        btnLogin = findViewById(R.id.btnLogin)
        Email = findViewById(R.id.LoginUsername)
        Password = findViewById(R.id.LoginPassword)
        SignUpPageLink = findViewById(R.id.Redirect2SignUp)
//The authentication vairable is initialized.
        auth = Firebase.auth
//The sign in request is created
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
//this button calls the method that logs the user in
        btnLogin.setOnClickListener {
            login()
        }
///this button redirects the user to the sign up page
        SignUpPageLink.setOnClickListener {
            navigateToSignUp()
        }
    }
//this method logs the user in
    private fun login() {
        //variables store the data from the activity
        val email = Email.text.toString()
        val pass = Password.text.toString()
//the user is signed in with firebase auth
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                navigateToAppHome() // Navigate to HomePage with flags
            } else {
                Toast.makeText(this, "Log In failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
//This method redirects the user to the home page
    private fun navigateToAppHome() {
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) // Clear stack and create new task
        startActivity(intent)
        finish() // Close MainActivity to ensure it's not in the stack
    }
//this method redirects the user to the sign up page
    private fun navigateToSignUp() {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }
}
