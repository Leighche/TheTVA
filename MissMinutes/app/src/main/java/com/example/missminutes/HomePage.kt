package com.example.missminutes

import android.content.Intent
<<<<<<< Updated upstream
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
=======
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val ratingPopUpDelay = 500  // Delay in milliseconds (5 seconds)

    override fun onCreate(savedContext: Bundle?) {
        super.onCreate(savedContext)
        setContentView(R.layout.activity_home_page)

        sharedPreferences = getSharedPreferences("MissMinutesPrefs", MODE_PRIVATE)
        val isRated = sharedPreferences.getBoolean("isRated", false)

        // Check if the pop-up should be shown
        if (!isRated) {
            // Add a log to confirm the delay is working
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                showRatingPopUp()  // Call function to create and show the pop-up
            }, ratingPopUpDelay.toLong())
        }

        // Initialize UI components
        val relax = findViewById<Button>(R.id.btnrelax)
        val TimeSheets = findViewById<Button>(R.id.BtnTimesheet)
        val CreateTask = findViewById<Button>(R.id.BtnCreateStudy)
        val Categorybtn = findViewById<Button>(R.id.BtnCreateCategory)

        // Navigation logic
        relax.setOnClickListener { navigateToPomodoro() }
        TimeSheets.setOnClickListener { navigateToTimesheets() }
        CreateTask.setOnClickListener { navigateToStudy() }
        Categorybtn.setOnClickListener { navigateToCategory() }
    }

    private fun showRatingPopUp() {
        val ratingPopUp = AlertDialog.Builder(this@HomePage)
        ratingPopUp.setTitle("Rate MissMinutes")
        ratingPopUp.setIcon(R.drawable.img_5)
        ratingPopUp.setMessage("Please take a moment to rate MissMinutes by clicking one of the following buttons")
        ratingPopUp.setCancelable(true)

        // Update SharedPreferences when the user rates
        ratingPopUp.setPositiveButton("Loving it!") { _, _ ->
            Toast.makeText(this, "We're glad you're enjoying MissMinutes!", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("isRated", true).apply()  // Set rated status to true
        }

        ratingPopUp.setNegativeButton("Hating it!") { _, _ ->
            Toast.makeText(this, "We're sorry to hear that you don't like MissMinutes, we will do better.", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("isRated", true).apply()  // Set rated status to true
        }

        ratingPopUp.create().show()
    }

    private fun navigateToPomodoro() {
        startActivity(Intent(this, Pomodoro::class.java))
        finish()
    }

    private fun navigateToCategory() {
        startActivity(Intent(this, Category::class.java))
        finish()
    }

    private fun navigateToTimesheets() {
        startActivity(Intent(this, Timesheet::class.java))
        finish()
    }

    private fun navigateToStudy() {
        startActivity(Intent(this, TaskPage::class.java))
        finish()
    }
}
>>>>>>> Stashed changes
