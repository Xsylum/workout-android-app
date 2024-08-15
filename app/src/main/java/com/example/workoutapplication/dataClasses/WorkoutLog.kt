package com.example.workoutapplication.dataClasses

import android.util.Log
import com.example.workoutapplication.dataClasses.ExerciseStats
import com.example.workoutapplication.dataClasses.Regimen
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

//TODO create a proxy class to make showing workouts on calendar dates easier
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
     * NOTE: If this method is called when workoutRegimen is null, it
     * will clear the workout's existing exercise stats
     */
    fun replaceExerciseStatsForNewRegimen() {
        exerciseStats.clear()

        if (workoutRegimen != null) {
            val exerciseList = workoutRegimen!!.exerciseList
            for (exercise in exerciseList) {
                val newExerciseStat = ExerciseStats(exercise, this)
                exerciseStats.add(newExerciseStat)
                newExerciseStat.addEmptySet()
                newExerciseStat.addEmptySet()
            }
            Log.d("Test", exerciseStats.toString())
        }
    }

}