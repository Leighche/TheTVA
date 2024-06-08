package com.example.missminutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.logging.Handler

class splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide();
//show splashscreen and redirects to MainActivity which is Login
        android.os.Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)//delays splash screen for 1500ms
    }
}
