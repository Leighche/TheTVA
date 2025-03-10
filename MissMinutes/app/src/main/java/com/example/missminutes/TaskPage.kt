package com.example.missminutes

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar


class TaskPage : AppCompatActivity() {

    private lateinit var buttonOpenDatePicker: Button
    private lateinit var buttonOpenEndDatePicker: Button
    private lateinit var NextTaskBtn: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_page)


        buttonOpenDatePicker = findViewById(R.id.startDate)
        buttonOpenEndDatePicker= findViewById(R.id.endDate)
        NextTaskBtn = findViewById(R.id.BtnNextTaskStep)


        buttonOpenDatePicker.setOnClickListener {
            openDatePicker()
        }

        buttonOpenEndDatePicker.setOnClickListener {
            openEndDatePicker()
        }


        NextTaskBtn.setOnClickListener {
            navigatToNextPage()
        }
    }

    private fun navigatToNextPage() {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }

    private fun openDatePicker() {
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

        // Set minimum date to 1 January 2023
        val minDate = Calendar.getInstance()
        minDate.set(2023, Calendar.JANUARY, 1)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        // Set maximum date to 31 December 2024
        val maxDate = Calendar.getInstance()
        maxDate.set(2024, Calendar.DECEMBER, 31)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }
    private fun openEndDatePicker() {
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

        // Set minimum date to 1 January 2023
        val minDate = Calendar.getInstance()
        minDate.set(2023, Calendar.JANUARY, 1)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        // Set maximum date to 31 December 2024
        val maxDate = Calendar.getInstance()
        maxDate.set(2024, Calendar.DECEMBER, 31)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }


}