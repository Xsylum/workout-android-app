package com.example.workoutapplication.dataClasses

import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

class ExerciseStatValue {

    lateinit var metricValID: UUID
    lateinit var exerciseMetric: ExerciseMetric

    var metricFormatMaxRange = 1;

    private var valStringFormat = ""
    private var valNumberFormat: Double? = null // metric Format 0
    private var valTimeFormat: TimeDuration? = null // metric Format 1

    constructor(exerciseMetric: ExerciseMetric) {
        this.exerciseMetric = exerciseMetric
        metricValID = UUID.randomUUID()
    }

    /**
     * Constructor for usage with the fromJsonString() companion method
     */
    constructor(metricValueID: UUID, exerciseMetric: ExerciseMetric, stringValue: String, formatValue:String) {

        this.metricValID = metricValueID
        this.valStringFormat = stringValue
        when (exerciseMetric.metricFormat) {
            0 -> valNumberFormat = formatValue.toDouble()
            1 -> valTimeFormat = TimeDuration.fromParsedString(formatValue)
        }
    }

    fun updateValue(stringValue: String) {
        valStringFormat = stringValue

        // updating non-string value
        when (exerciseMetric.metricFormat) {
            0 -> valNumberFormat = stringValue.toDouble()
            1 -> valTimeFormat = TimeDuration.fromParsedString(stringValue)
        }
    }

    fun getStringValue(): String {
        return valStringFormat
    }

    /** Non-String Getters **/
    fun getDoubleValue(): Double {
        if (exerciseMetric.metricFormat != 0) {
            throw IllegalStateException("Cannot call getDoubleValue as metricFormat != 0" +
                    "(metricFormat = ${exerciseMetric.metricFormat}")
        }

        return valNumberFormat!!
    }

    fun getTimeValue(): TimeDuration {
        if (exerciseMetric.metricFormat != 1) {
            throw IllegalStateException("Cannot call getTimeValue as metricFormat != 0" +
                    "(metricFormat = ${exerciseMetric.metricFormat}")
        }

        return valTimeFormat!!
    }

    /** DataStore Methods **/
    fun toJsonString(): String {
        val jsonObject = JSONObject()

        try {
            jsonObject.put("UniqueID", metricValID.toString())
            jsonObject.put("MetricID", exerciseMetric.metricID.toString())
            jsonObject.put("StringValue", valStringFormat)
            jsonObject.put("FormatValue", when (exerciseMetric.metricFormat) {
                                                    0 -> valNumberFormat.toString()
                                                    else -> valTimeFormat.toString()
                                                })
        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for metricValue $metricValID"
        }

        return jsonObject.toString()
    }

    companion object {
        fun fromJsonString(jsonString: String, exerciseMetrics: List<ExerciseMetric>)
        : ExerciseStatValue {
            try {
                val jsonMetric = JSONObject(jsonString)

                val exerciseMetricID = UUID.fromString(jsonMetric.get("MetricID").toString())
                val parentMetric = exerciseMetrics.first {eM -> eM.metricID == exerciseMetricID}

                val metricValueID = UUID.fromString(jsonMetric.get("UniqueID").toString())
                val stringValue = jsonMetric.get("StringValue").toString()
                val formatValue = jsonMetric.get("formatValue").toString()

                return ExerciseStatValue(metricValueID, parentMetric, stringValue, formatValue)
            } catch (e: JSONException) {
                e.printStackTrace()
                throw IllegalArgumentException("The input JSONString is invalid!")
            }
        }
    }
}