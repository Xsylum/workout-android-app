package com.example.workoutapplication.dataClasses

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

/**
 * Simple data class matching a metric name with a boolean, clarifying if the
 * stat is tracking a time or a Float (and the measurement unit of either type)
 */
class ExerciseMetric()
    : Parcelable {

    lateinit var metricID: UUID
        private set
    var metricName: String = ""
    var metricFormat: Int = -1
    var metricFormatMaxRange = 1; // [0, x]
    var measurementUnit: String = ""

    // Format 0 -> number
    // Format 1 -> Time


    constructor(metricName: String, metricFormat: Int,
                measurementUnit: String, metricID: UUID = UUID.randomUUID()) : this() {
        if (0 > metricFormat || metricFormat > metricFormatMaxRange) {
            throw IllegalArgumentException("desiredFormat must be between" +
                    "0 and $metricFormatMaxRange (inclusive)")
        }

        this.metricName = metricName
        this.metricFormat = metricFormat
        this.measurementUnit = measurementUnit
        this.metricID = metricID
    }

    constructor(parcel: Parcel) : this(
        metricName = parcel.readString()!!,
        metricFormat = parcel.readInt()!!,
        measurementUnit = parcel.readString()!!,
        metricID = UUID.fromString(parcel.readString()))

    fun getFormatTypeAsString(): String {
        return when (metricFormat) {
            0 -> "Number"
            1 -> "Time"
            else -> "Erroneous Format Type!"
        }
    }

    fun toJsonString(): String {
        val jsonMetric = JSONObject()
        try {
            jsonMetric.put("UniqueID", metricID.toString())
            jsonMetric.put("Name", metricName)
            jsonMetric.put("MetricFormat", metricFormat)
            jsonMetric.put("MeasurementUnit", measurementUnit)

        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for metric $metricID"
        }

        return jsonMetric.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(metricName)
        parcel.writeInt(metricFormat)
        parcel.writeString(measurementUnit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        fun fromJsonString(jsonString: String?): ExerciseMetric {
            try {
                val jsonMetric = JSONObject(jsonString)

                val metricID = UUID.fromString(jsonMetric.get("UniqueID").toString())
                val metricName = jsonMetric.get("Name").toString()
                val metricFormat = jsonMetric.get("MetricFormat").toString().toInt()
                val measurementUnit = jsonMetric.get("MeasurementUnit").toString()

                return ExerciseMetric(metricName, metricFormat, measurementUnit, metricID)
            } catch (e: JSONException) {
                e.printStackTrace()
                throw IllegalArgumentException("The input JSONString is invalid!")
            }
        }

        @JvmField
        val CREATOR : Parcelable.Creator<ExerciseMetric> = object : Parcelable.Creator<ExerciseMetric> {
            override fun createFromParcel(parcel: Parcel): ExerciseMetric {
                return ExerciseMetric(parcel)
            }

            override fun newArray(size: Int): Array<ExerciseMetric?> {
                return arrayOfNulls(size)
            }
        }
    }
}