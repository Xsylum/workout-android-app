package com.example.workoutapplication.dataClasses

import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

/**
 * Simple data class matching a metric name with a boolean, clarifying if the
 * stat is tracking a time or a Float (and the measurement unit of either type)
 */
class ExerciseMetric(val metricName: String, val isTimeStat: Boolean,
                     val measurementUnit: String = "", val metricID: UUID = UUID.randomUUID()) {

    fun toJsonString(): String {
        val jsonMetric = JSONObject()
        try {
            jsonMetric.put("UniqueID", metricID.toString())
            jsonMetric.put("Name", metricName)
            jsonMetric.put("IsTimeStat", isTimeStat)
            jsonMetric.put("MeasurementUnit", measurementUnit)

        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for metric $metricID"
        }

        return jsonMetric.toString()
    }

    companion object {
        fun fromJsonString(jsonString: String?): ExerciseMetric {
            try {
                val jsonMetric = JSONObject(jsonString)

                val metricID = UUID.fromString(jsonMetric.get("UniqueID").toString())
                val metricName = jsonMetric.get("Name").toString()
                val isTimeStat = jsonMetric.get("IsTimeStat").toString().toBoolean()
                val measurementUnit = jsonMetric.get("MeasurementUnit").toString()

                return ExerciseMetric(metricName, isTimeStat, measurementUnit, metricID)
            } catch (e: JSONException) {
                e.printStackTrace()
                throw IllegalArgumentException("The input JSONString is invalid!")
            }
        }
    }
}