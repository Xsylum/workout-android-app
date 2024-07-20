package com.example.workoutapplication

import java.time.LocalDate
import java.time.LocalTime

class WorkoutLog (var date: LocalDate) {
    private val timeOfCompletion: LocalTime? = null
    private val workoutRegimen: Regimen? = null
    private val exerciseStats: ArrayList<ExerciseStats>? = null

    fun setDateWithInts(year: Int, month: Int, dayOfMonth: Int) {
        date = LocalDate.of(year, month, dayOfMonth)
    }
}