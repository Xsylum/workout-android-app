package com.example.workoutapplication.dataClasses

class ExerciseStats (private val exercise: Exercise, private val partOfWorkout: WorkoutLog) {
    //TODO: check that length is correct! (don't allow 1 where there is no metric definition!)
    var measureMetricsBitMap = 0 // THIS WILL BE A BINARY VALUE

    // TODO: implement method, and possibly a facade for toggling metrics by string
    fun toggleMetricInMap(metric: Int) {}
}