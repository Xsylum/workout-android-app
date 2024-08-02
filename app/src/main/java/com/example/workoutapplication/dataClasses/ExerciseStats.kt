package com.example.workoutapplication.dataClasses

import androidx.core.util.toAndroidXPair
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

class ExerciseStats (private val exercise: Exercise,
                     private val partOfWorkout: WorkoutLog,
                     private val eStatsID: UUID = UUID.randomUUID()) {

    private val trackingMetrics = exercise.trackingMetrics

    // An array list holding multiple Sets (as in, "each set has x reps"),
    // with each set pairing each metric from trackingMetrics to some datum
    private val metricData = ArrayList<ExerciseSet>()

    /**
     * Subclass for code clarity, representing an single exercise's set.
     *
     * When reading or writing to DataStore, this class is responsible for matching
     * metric and metricValues string UUIDs with the relevant objects
     */
    internal inner class ExerciseSet: ArrayList<Pair<ExerciseMetric, ExerciseMetricValue>>() {
        fun toJsonString(): String {
            return try {
                val jsonArray = JSONArray()

                for ((metric, metricValue) in this) {
                    // Using the below string template to avoid cost of performing
                    // JSONArray(pair).toString() for each pair
                    val arrayedInfo = "[\"${metric.metricID}\",${metricValue.metricValID}]"
                    jsonArray.put(arrayedInfo)
                }
                jsonArray.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
                "Error converting ExerciseSet to jsonString!"
            }
        }

        /**
         * Replaces the contents of this ExerciseSet with the contents of
         * the jsonString, with its UUIDs matched to the relevant ExerciseMetric
         * or ExerciseMetricValue objects
         *
         * WARNING: This will PERMANENTLY WIPE the existing data of
         * the ExerciseSet it is called on!
         */
        fun fillFromJsonString(jsonString: String,
                                       metricList: List<ExerciseMetric>,
                                       valueList: List<ExerciseMetricValue>) {
            this.clear()
            val jsonArray = JSONArray(jsonString)

            for (i in 0..jsonArray.length()) {
                // get the current pair from the overall array
                val pairArray = JSONArray(jsonArray.get(i))

                // find the metric & metricValue from respective lists with
                // the uniqueIDs as listed in pairArray
                val metric = metricList.first {m ->
                    m.metricID == UUID.fromString(pairArray.get(0).toString())}
                val metricValue = valueList.first {v ->
                    v.metricValID == UUID.fromString(pairArray.get(1).toString())}

                // Create the pair and add it to this ExerciseSet
                val pair = Pair(metric, metricValue)
                this.add(pair)
            }
        }

    }

    private fun addEmptySet() {
        val newExerciseSet = basicExerciseSet()

        metricData.add(newExerciseSet)
    }

    private fun addSet(set: ExerciseSet) {
        metricData.add(set)
    }

    private fun editSetMetricValue(set: Int, metricPosition: Int, newStringValue: String) {
        metricData[set][metricPosition].second.updateValue(newStringValue)
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

    fun toJsonString(): String {
        return try {
            val outputJson = JSONObject()

            outputJson.put("UniqueID", eStatsID.toString())
            outputJson.put("Exercise", exercise.exerciseID)
            outputJson.put("Workout", partOfWorkout.workoutID)

            val exerciseSetsJson = JSONArray(metricData)
            outputJson.put("ExerciseSets", exerciseSetsJson.toString())

            outputJson.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
                "Error converting ExerciseStats $eStatsID to json string!"
            }
    }

    companion object {
        fun fromJsonString(jsonString: String,
                           exerciseList: List<Exercise>,
                           workoutList: List<WorkoutLog>,
                           metricList: List<ExerciseMetric>,
                           valueList: List<ExerciseMetricValue>): ExerciseStats {
            val jsonObject = JSONObject(jsonString)

            // Retrieve related exercise and workout from relevant lists
            val exerciseID = jsonObject.get("Exercise").toString()
            val exercise = exerciseList.first {e -> e.exerciseID == UUID.fromString(exerciseID)}
            val workoutID = jsonObject.get("Workout").toString()
            val workout = workoutList.first {w -> w.workoutID == UUID.fromString(workoutID)}

            //Create the output ExerciseStats
            val output = ExerciseStats(exercise, workout,
                UUID.fromString(jsonObject.get("UniqueID").toString()))

            // Fill out the ExerciseStats sets
            val exerciseSets = JSONArray(jsonObject.get("ExerciseSets").toString())
            for (i in 0..exerciseSets.length()) {
                val set = output.ExerciseSet()
                set.fillFromJsonString(exerciseSets[i].toString(), metricList, valueList)
                output.addSet(set)
            }

            return output
        }
    }
}