package com.example.missminutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HomePage : AppCompatActivity() {

    private lateinit var CreateTask: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        CreateTask = findViewById(R.id.BtnCreateStudy)


        CreateTask.setOnClickListener {
            navigateToStudy()
        }
    }

        private fun navigateToStudy() {
            val intent = Intent(this, TaskPage::class.java)
            startActivity(intent)
            finish()
        }
    }
