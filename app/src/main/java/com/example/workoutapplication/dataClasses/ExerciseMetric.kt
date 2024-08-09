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
class ExerciseMetric(val metricName: String, val isTimeStat: Boolean,
                     val measurementUnit: String = "", val metricID: UUID = UUID.randomUUID())
    : Parcelable {

    constructor(parcel: Parcel) : this(
        metricName = parcel.readString()!!,
        isTimeStat = parcel.readByte() != 0.toByte(),
        measurementUnit = parcel.readString()!!,
        metricID = UUID.fromString(parcel.readString()))

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(metricName)
        parcel.writeByte(if (isTimeStat) 1 else 0)
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
                val isTimeStat = jsonMetric.get("IsTimeStat").toString().toBoolean()
                val measurementUnit = jsonMetric.get("MeasurementUnit").toString()

                return ExerciseMetric(metricName, isTimeStat, measurementUnit, metricID)
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