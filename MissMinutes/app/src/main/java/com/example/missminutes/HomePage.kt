package com.example.missminutes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class HomePage : AppCompatActivity() {

    private lateinit var CreateTask: Button
    private lateinit var TimeSheets :Button
    private lateinit var relax : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
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
        val RatingPopUp = AlertDialog.Builder(this@HomePage)
//This sets the title
        RatingPopUp.setTitle("Rate MissMinutes")
//This sets the icon of the pop up box
        RatingPopUp.setIcon(R.drawable.img_5)
//This sets the text of the pop up
        RatingPopUp.setMessage("Please take a moment to rate MissMinutes by clicking one of the following buttons")
//This makes the pop up cancelable
        RatingPopUp.setCancelable(true)
//This is the actions that will be taken if the user clicks the positive button
        RatingPopUp.setPositiveButton("Loving it!"){_,_ ->
//this will be displayed to the user if they click the positive button
            Toast.makeText(this@HomePage, "We're glad you're enjoying MissMinutes!", Toast.LENGTH_LONG).show()
        }
//This is the actions that will be taken if the user clicks the negative button
        RatingPopUp.setNegativeButton("Hating it!"){_,_ ->
//This message will be displayed to the user if they click the negative button.
            Toast.makeText(this@HomePage, "We're sorry to hear that you don't like MissMinutes, we will do better.", Toast.LENGTH_LONG).show()
        }
//The pop up boxes is created and displayed to the user
        val PopUp = RatingPopUp.create()
        PopUp.show()
        relax =  findViewById(R.id.btnrelax)

        TimeSheets = findViewById(R.id.BtnTimesheet)

        CreateTask = findViewById(R.id.BtnCreateStudy)

        relax.setOnClickListener{
            navigateToPomodoro()
        }

        TimeSheets.setOnClickListener{
            navigateToTimesheets()
        }


        CreateTask.setOnClickListener {
            navigateToStudy()
        }
    }

    private fun navigateToPomodoro() {
        val intent = Intent(this, Pomodoro::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToTimesheets() {
        val intent = Intent(this, Timesheet::class.java)
        startActivity(intent)
        finish()

    }

    private fun navigateToStudy() {
            val intent = Intent(this, TaskPage::class.java)
            startActivity(intent)
            finish()
        }
    }
