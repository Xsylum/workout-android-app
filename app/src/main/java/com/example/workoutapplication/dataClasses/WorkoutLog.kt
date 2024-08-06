package com.example.workoutapplication.dataClasses

import com.example.workoutapplication.dataClasses.ExerciseStats
import com.example.workoutapplication.dataClasses.Regimen
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class WorkoutLog (var date: LocalDate = LocalDate.now(), val workoutID: UUID = UUID.randomUUID()) {
    var timeOfCompletion: LocalTime? = null
        private set
    var workoutRegimen: Regimen? = null
    val exerciseStats = ArrayList<ExerciseStats>()

    fun setDateWithInts(year: Int, month: Int, dayOfMonth: Int) {
        date = LocalDate.of(year, month, dayOfMonth)
    }

    /**
     * Clears existing ExerciseStats from this WorkoutLog and fills it with new
     * ExerciseStats based on the exercises in workoutRegimen
     *
     * This method should only be called when workoutRegimen has been assigned,
     * calling it otherwise will throw an IllegalStateException
     */
    fun replaceExerciseStatsForNewRegimen() {
        if (workoutRegimen == null) {
            throw IllegalStateException("Cannot fill WorkoutLog.exerciseStats with a null regimen!")
        }

        exerciseStats.clear()
        val exerciseList = workoutRegimen!!.exerciseList
        for (exercise in exerciseList) {
            exerciseStats.add(ExerciseStats(exercise, this))
        }
    }

}