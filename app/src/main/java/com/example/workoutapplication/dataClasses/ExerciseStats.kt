package com.example.workoutapplication.dataClasses

import androidx.core.util.toAndroidXPair

class ExerciseStats (private val exercise: Exercise, private val partOfWorkout: WorkoutLog) {

    private val trackingMetrics = exercise.trackingMetrics

    // An array list holding multiple Sets (as in, "each set has x reps"),
    // with each set pairing each metric from trackingMetrics to some datum
    private val metricStringData = ArrayList<ExerciseSet>()

    inner class ExerciseSet: ArrayList<Pair<ExerciseMetric, ExerciseMetricValue>>()

    private fun addSet() {
        val newExerciseSet = basicExerciseSet()

        metricStringData.add(newExerciseSet)
    }

    private fun editSetMetricValue(set: Int, metricPosition: Int, newStringValue: String) {
        metricStringData[set][metricPosition].second.updateValue(newStringValue)
    }

    /**
     * Creates a default "set" (as in "each set has x reps"), for this
     * exercise's tracking metrics.
     *
     * Each of the exercise metrics is paired with an empty String value,
     * allowing the values to be set by the user later on.
     */
    private fun basicExerciseSet(): ExerciseSet {
        val outputList = ExerciseSet()

        for (metric in trackingMetrics) {
            val metricValue = ExerciseMetricValue(format = if (metric.isTimeStat) 1 else 0)
            val metricPair = Pair(metric, metricValue)
            outputList.add(metricPair)
        }

        return outputList
    }
}