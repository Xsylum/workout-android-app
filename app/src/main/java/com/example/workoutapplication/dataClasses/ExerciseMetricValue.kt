package com.example.workoutapplication.dataClasses

import kotlin.time.Duration

class ExerciseMetricValue {

    var metricFormat = -1;
    var metricFormatMaxRange = 1;

    var stringValFormat = ""
    var numberValFormat: Double? = null // metric Format 0
    var timeValFormat: Duration? = null // metric Format 1

    constructor(format: Int) {
        if (0 > format || format > metricFormatMaxRange) {
            throw IllegalArgumentException("desiredFormat must be between" +
                    "0 and $metricFormatMaxRange (inclusive)")
        }

        metricFormat = format
    }
}