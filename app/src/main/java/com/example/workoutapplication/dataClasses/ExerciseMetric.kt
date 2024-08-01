package com.example.workoutapplication.dataClasses

/**
 * Simple data class matching a metric name with a boolean, clarifying if the
 * stat is tracking a time or a Float (and the measurement unit of either type)
 */
data class ExerciseMetric(val metricName: String, val isTimeStat: Boolean,
                          val measurementUnit: String = "")