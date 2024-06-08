package com.example.missminutes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay

class HomePage : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedContext: Bundle?) {
        super.onCreate(savedContext)
        setContentView(R.layout.activity_home_page)

        sharedPreferences = getSharedPreferences("MissMinutesPrefs", MODE_PRIVATE)
        val isRated = sharedPreferences.getBoolean("isRated", false)
        Log.d("HomePage", "isRated: $isRated")  // Log to check isRated value

        resetSharedPreferences()  // If needed, but ensure sharedPreferences is initialized

        // Check if the pop-up should be shown
        if (!isRated) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // Only show the pop-up if the activity is not finishing or destroyed
                if (!isFinishing && !isDestroyed) {
                    showRatingPopUp()
                }
            }, 5000)  // Delay for the pop-up
        }


        // Initialize UI components
        val relax = findViewById<Button>(R.id.btnrelax)
        val TimeSheets = findViewById<Button>(R.id.BtnTimesheet)
        val CreateTask = findViewById<Button>(R.id.BtnCreateStudy)
        val Categorybtn = findViewById<Button>(R.id.BtnCreateCategory)
        val statistics = findViewById<Button>(R.id.BtnStatistics)

        // Navigation logic
        relax.setOnClickListener { navigateToPomodoro() }
        TimeSheets.setOnClickListener { navigateToTimesheets() }
        CreateTask.setOnClickListener { navigateToStudy() }
        Categorybtn.setOnClickListener { navigateToCategory() }
        statistics.setOnClickListener { navigateToStatistics() }
    }



    private fun showRatingPopUp() {
        Log.d("HomePage", "Inside showRatingPopUp")

        //Code Attribution
//This code was adapted from Learn with Deeksha's YouTube video called
//Alert Dialog Box in Android using Kotlin | Kotlin | Android Studio Tutorial - Quick + Easy
// The link to the video: https://youtu.be/V-qjrWuUFrQ
//The link to the YouTube channel: https://www.youtube.com/@learnwithdeeksha9996
//The code:
//val artDialogBuilder = AlertDialog.Builder(this@MainActivity)
//artDialogBuilder.setTitle("Title")
// artDialogBuilder.setMessage("Are you sure you want to exit?")
// artDialogBuilder.setCancelable(false)
//artDialogBuilder.setPositiveButton("Yes"){_,_ ->
//
// finish() }
//
// artDialogBuilder.setNegativeButton("No"){_,_ ->
//
// Toast.makeText(this@MainActivity, "Clicked No", Toast.LENGTH_LONG).show()
// }
// val alertDialogBox = artDialogBuilder.create()
// alertDialogBox.show()
        val ratingPopUp = AlertDialog.Builder(this@HomePage)
//This sets the title
        ratingPopUp.setTitle("Rate MissMinutes")
//This sets the icon of the pop up box
        ratingPopUp.setIcon(R.drawable.img_5)
//This sets the text of the pop up
        ratingPopUp.setMessage("Please take a moment to rate MissMinutes by clicking one of the following buttons")
//This makes the pop up cancelable
        ratingPopUp.setCancelable(true)
//This is the actions that will be taken if the user clicks the positive button
        ratingPopUp.setPositiveButton("Loving it!") { _, _ ->
            Toast.makeText(this@HomePage, "Glad you like it!", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("isRated", true).apply()
        }

        ratingPopUp.setNegativeButton("Hating it!") { _, _ ->
            Toast.makeText(this@HomePage, "Sorry to hear that!", Toast.LENGTH_LONG).show()
            sharedPreferences.edit().putBoolean("isRated", true).apply()
        }
//The pop up boxes is created and displayed to the user
        val popUp = ratingPopUp.create()
        popUp.show()  // Make sure this line is executed
    }
//This method redirects to the pomodor page
    private fun navigateToPomodoro() {
        startActivity(Intent(this, Pomodoro::class.java))
        finish()
    }
//This method redirects to the category page
    private fun navigateToCategory() {
        startActivity(Intent(this, Category::class.java))
        finish()
    }
//This method redirects to the timesheet page
    private fun navigateToTimesheets() {
        startActivity(Intent(this, Timesheet::class.java))
        finish()
    }

    //This method redirects to the task page.
    private fun navigateToStudy() {
        startActivity(Intent(this, TaskPage::class.java))
        finish()
    }
//This emthod redirects to the statisctic page
    private fun navigateToStatistics() {
        startActivity(Intent(this, CategoryHours::class.java))
        finish()
    }
    private fun resetSharedPreferences() {
        sharedPreferences.edit().clear().apply()  // Clears all preferences
    }

}
