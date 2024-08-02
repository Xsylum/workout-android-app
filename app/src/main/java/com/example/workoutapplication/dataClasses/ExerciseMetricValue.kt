package com.example.workoutapplication.dataClasses

import org.json.JSONException
import org.json.JSONObject
import java.util.UUID
import kotlin.time.Duration

class ExerciseMetricValue {

    lateinit var metricValID: UUID

    var metricFormat = -1;
    var metricFormatMaxRange = 1;

    var stringValFormat = ""
        private set
    private var numberValFormat: Double? = null // metric Format 0
    private var timeValFormat: TimeDuration? = null // metric Format 1

    constructor(format: Int) {
        if (0 > format || format > metricFormatMaxRange) {
            throw IllegalArgumentException("desiredFormat must be between" +
                    "0 and $metricFormatMaxRange (inclusive)")
        }

        metricFormat = format
        metricValID = UUID.randomUUID()
    }

    /**
     * Constructor for usage with the fromJsonString() companion method
     */
    constructor(metricValueID: UUID, metricFormat: Int, stringValue: String, formatValue:String) {
        if (0 > metricFormat || metricFormat > metricFormatMaxRange) {
            throw IllegalArgumentException("desiredFormat must be between" +
                    "0 and $metricFormatMaxRange (inclusive)")
        }

        this.metricValID = metricValueID
        this.metricFormat = metricFormat
        this.stringValFormat = stringValue
        when (metricFormat) {
            0 -> numberValFormat = formatValue.toDouble()
            1 -> timeValFormat = TimeDuration.fromParsedString(formatValue)
        }
    }

    fun updateValue(stringValue: String) {
        stringValFormat = stringValue

        // updating non-string value
        when (metricFormat) {
            0 -> numberValFormat = stringValue.toDouble()
            1 -> timeValFormat = TimeDuration.fromParsedString(stringValue)
        }
    }

    fun getStringValue(): String {
        return stringValFormat
    }

    /** Non-String Getters **/
    fun getDoubleValue(): Double {
        if (metricFormat != 0) {
            throw IllegalStateException("Cannot call getDoubleValue as metricFormat != 0" +
                    "(metricFormat = $metricFormat")
        }

        return numberValFormat!!
    }

    fun getTimeValue(): TimeDuration {
        if (metricFormat != 1) {
            throw IllegalStateException("Cannot call getTimeValue as metricFormat != 0" +
                    "(metricFormat = $metricFormat")
        }

        return timeValFormat!!
    }

    /** DataStore Methods **/
    fun toJsonString(): String {
        val jsonObject = JSONObject()

        try {
            jsonObject.put("UniqueID", metricValID)
            jsonObject.put("ValueFormat", metricFormat)
            jsonObject.put("StringValue", stringValFormat)
            jsonObject.put("FormatValue", when (metricFormat) {
                                                    0 -> numberValFormat.toString()
                                                    else -> timeValFormat.toString()
                                                })
        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for metricValue $metricValID"
        }

        return jsonObject.toString()
    }

    companion object {
        fun fromJsonString(jsonString: String): ExerciseMetricValue {
            try {
                val jsonMetric = JSONObject(jsonString)

                val metricValueID = UUID.fromString(jsonMetric.get("UniqueID").toString())
                val valueFormat = jsonMetric.get("ValueFormat").toString().toInt()
                val stringValue = jsonMetric.get("StringValue").toString()
                val formatValue = jsonMetric.get("formatValue").toString()

                return ExerciseMetricValue(metricValueID, valueFormat, stringValue, formatValue)
            } catch (e: JSONException) {
                e.printStackTrace()
                throw IllegalArgumentException("The input JSONString is invalid!")
            }
        }
    }
}