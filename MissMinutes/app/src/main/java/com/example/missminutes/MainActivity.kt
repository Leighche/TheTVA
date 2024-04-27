package com.example.missminutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: EditText
    private var onetapclient: SignInClient?=null
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var btnLogin: Button
    private lateinit var SignUpPageLink: TextView
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        btnLogin = findViewById(R.id.btnLogin)
        Email = findViewById(R.id.LoginUsername)
        Password = findViewById(R.id.LoginPassword)
        SignUpPageLink = findViewById(R.id.Redirect2SignUp)

        auth = Firebase.auth
        // Initializing Firebase auth object
        auth = FirebaseAuth.getInstance()

        onetapclient= Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        btnLogin.setOnClickListener {
            login()
        }

        SignUpPageLink.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun login() {
        val email = Email.text.toString()
        val pass = Password.text.toString()

        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                navigateToAppHome()
            }
            else {
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

        private fun navigateToSignUp() {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
    private fun navigateToAppHome() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

    }

