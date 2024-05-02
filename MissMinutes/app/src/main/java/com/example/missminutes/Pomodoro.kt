package com.example.missminutes
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
class Pomodoro : AppCompatActivity() {
    //Code Attribution
//This code was adapted from GeeksForGeeks
//https://www.geeksforgeeks.org/how-to-build-a-pomodoro-app-in-android/
//The following code has been adapted:
//start_button.setOnClickListener{
// if (!timerRunning) {
// startTimer()
// timerRunning = true
// start_button.text = "Pause"
// }
// else {
// pomodoroTimer?.cancel()
// timerRunning = false
// start_button.text = "Start"
// }
// }
//private fun startTimer() {
// val timerLength : Long = 25 * 60 * 1000
// pomodoroTimer = object : CountDownTimer(timerLength,1000){
// override fun onTick(millisUntilFinished: Long) {
// val minutes = millisUntilFinished / 1000 / 60
// val seconds = millisUntilFinished / 1000 % 60
// timer_text_view.text = "$minutes:$seconds"
// }
//
// override fun onFinish() {
// pomodoroTimer?.cancel()
// timerRunning = false
// // Display the dialog
// val builder = AlertDialog.Builder(this@MainActivity)
// builder.setMessage("Start a 5-minute break timer?")
// .setPositiveButton("Yes") { _, _ -> startBreakTimer() }
// .setNegativeButton("No") { _, _ ->
//
// startTimer()
// timerRunning = true
// start_button.text = "Pause"
// }
// .show()
//
//
//
// }
//
// }
// pomodoroTimer?.start()
// }
//
    private lateinit var Cancel: Button
    private lateinit var timer: TextView
    private var pomodoroTimer: CountDownTimer? = null
    private var timerRunning = true
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)
//This calls the method to start the timer.
        startTimer()
//This assigns the cancel button to a variable
        Cancel = findViewById(R.id.BtnCancelBreak)

        Cancel.setOnClickListener {
//This redirects the user to a new activity
            val intent = Intent(this, TaskPage::class.java)
            startActivity(intent)
        }
    }
    //This method starts the timer, sets the time and displays the amount of time remaining.
    private fun startTimer() {
        val textView: TextView = findViewById(R.id.time_text_view) as TextView
        val timerLength : Long = 25 * 60 * 1000
        pomodoroTimer = object : CountDownTimer(timerLength,1000){
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                textView.text = "$minutes:$seconds"
            }
            //This method stops the timer once it ends
            override fun onFinish() {
                pomodoroTimer?.cancel()
                timerRunning = false
            }

        }
//The start method is called.
        pomodoroTimer?.start()
    }

}