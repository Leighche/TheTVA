package com.example.missminutes

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class TaskPage : AppCompatActivity() {
//The following variables will store the user's input from this activity
    private lateinit var buttonOpenDatePicker: Button
    private lateinit var buttonOpenEndDatePicker: Button
    private lateinit var NextTaskBtn: TextView
    private lateinit var Obj: EditText
    private lateinit var Desc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_page)
//The usre's input is saved in the following variables
        Obj = findViewById(R.id.ObjectiveTxtbox)
        Desc = findViewById(R.id.DescriptionTxtbox)
        buttonOpenDatePicker = findViewById(R.id.startDate)
        buttonOpenEndDatePicker = findViewById(R.id.endDate)
        NextTaskBtn = findViewById(R.id.BtnNextTaskStep)

        //This on click listener call the method that opens the start date picker.
        buttonOpenDatePicker.setOnClickListener {
            openDatePicker()
        }

        //This onclick listener calls the method that opens the end date picker
        buttonOpenEndDatePicker.setOnClickListener {
            openEndDatePicker()
        }
//This on click listener checks for valid inputs by the user and redirect the user to the finalize task page
        NextTaskBtn.setOnClickListener {
            try {
                validateInputs()  // Check if inputs are valid
                navigateToNextPage()  // Navigate to the next page
            } catch (e: Exception) {
                //If there are any errors, they are displayed to the user.
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()  // Display error message
            }
        }
    }

    override fun onBackPressed() {
        // Navigate to HomePage when back button is pressed
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

        // Call super to ensure default back behavior is executed
        super.onBackPressed()
    }

    private fun validateInputs() {
        // Validate that the text fields are not empty
        if (Obj.text.isNullOrBlank()) {
            throw IllegalArgumentException("Objective cannot be empty")
        }
        if (Desc.text.isNullOrBlank()) {
            throw IllegalArgumentException("Description cannot be empty")
        }

        // Validate dates (optional example using SimpleDateFormat)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            dateFormat.parse(buttonOpenDatePicker.text.toString())
            dateFormat.parse(buttonOpenEndDatePicker.text.toString())
        } catch (e: ParseException) {
            throw IllegalArgumentException("Invalid date format")
        }
    }
//This method saves this activity's info in a bundle which will be passed to the next activity
    //It also redirects the user to the finalizeTask page
    private fun navigateToNextPage() {
        val bundle = Bundle()
        bundle.putString("objective", Obj.text.toString())
        bundle.putString("description", Desc.text.toString())
        bundle.putString("startDate", buttonOpenDatePicker.text.toString())
        bundle.putString("endDate", buttonOpenEndDatePicker.text.toString())

        val intent = Intent(this, FinaliseTask::class.java)

        // Add error handling for intent
        try {
            intent.putExtras(bundle)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("TaskPage", "Error starting FinaliseTask activity", e)  // Log the error
            throw IllegalStateException("Could not navigate to FinaliseTask")
        }
    }
//This method opens the start date picker, sets it parameters, and the min and max dates that the user can choose.
    private fun openDatePicker() {
        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    buttonOpenDatePicker.text = selectedDate
                },
                year,
                month,
                day
            )

            // Set minimum and maximum dates
            val minDate = Calendar.getInstance().apply {
                set(2023, Calendar.JANUARY, 1)
            }
            val maxDate = Calendar.getInstance().apply {
                set(2024, Calendar.DECEMBER, 31)
            }
            datePickerDialog.datePicker.minDate = minDate.timeInMillis
            datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

            datePickerDialog.show()

        } catch (e: Exception) {
            Toast.makeText(this, "Error opening date picker: ${e.message}", Toast.LENGTH_LONG).show()  // Handle date picker error
        }
    }
//This method opens the end date picker, sets it's parameters, the max and min dates that the user can select
    private fun openEndDatePicker() {
        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    buttonOpenEndDatePicker.text = selectedDate
                },
                year,
                month,
                day
            )

            val minDate = Calendar.getInstance().apply {
                set(2023, Calendar.JANUARY, 1)
            }
            val maxDate = Calendar.getInstance().apply {
                set(2024, Calendar.DECEMBER, 31)
            }
            datePickerDialog.datePicker.minDate = minDate.timeInMillis
            datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

            datePickerDialog.show()

        } catch (e: Exception) {
            Toast.makeText(this, "Error opening end date picker: ${e.message}", Toast.LENGTH_LONG).show()  // Handle error
        }
    }
}
