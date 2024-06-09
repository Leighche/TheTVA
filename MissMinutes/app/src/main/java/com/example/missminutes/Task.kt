package com.example.missminutes

//This data class represents a task and all of its information
data class Task(var categoryName: String ?= null, var description:String ?=null, var endDate: String ?= null, var maxGoal: String ?= null, var minGoal: String ?= null,var objective: String ?= null,var startDate: String ?= null, var taskImage :String ?= null, var hours: Long?= null)

//This data class represents a category and it's total hours
data class CategorySummary(
    val categoryName: String,
    val totalHours: Double
)
