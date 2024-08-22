package com.example.workoutapplication.dataClasses

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

class ExerciseStats (val exercise: Exercise,
                     val partOfWorkout: UUID, // workout ID
                     val eStatsID: UUID = UUID.randomUUID()) {

    // NOTE: this value should stay updated with
    // the ordering in metricDataGrid to maintain data accuracy!!
    val trackingMetrics = exercise.trackingMetrics

    // metricDataGrid[position] gives the metric value for a single exercise set
    // metricDataGrid[position][x] gives the metricValue for the x-th metric of the exercise
    val metricDataGrid = ArrayList<ArrayList<ExerciseStatValue>>()


    fun addEmptySet() {
        val newExerciseSet = basicExerciseSet()

        metricDataGrid.add(newExerciseSet)
    }

    private fun addSet(set: ArrayList<ExerciseStatValue>) {
        metricDataGrid.add(set)
    }

    private fun editSetMetricValue(set: Int, metricPosition: Int, newStringValue: String) {
        metricDataGrid[set][metricPosition].updateValue(newStringValue)
    }

    /**
     * Creates a default "set" (as in "each set has x reps"), for this
     * exercise's tracking metrics.
     *
     * Each of the exercise metrics is paired with an empty String value,
     * allowing the values to be set by the user later on.
     */
    private fun basicExerciseSet(): ArrayList<ExerciseStatValue> {
        val outputList = ArrayList<ExerciseStatValue>()

        for (metric in trackingMetrics) {
            val metricValue = ExerciseStatValue(metric)
            outputList.add(metricValue)
        }

        return outputList
    }

    /**
     * Converts this class to a parsed JSONString for DataStore usage
     */
    fun toJsonString(): String {
        return try {
            val outputJson = JSONObject()

            outputJson.put("UniqueID", eStatsID.toString())
            outputJson.put("Exercise", exercise.exerciseID)
            outputJson.put("Workout", partOfWorkout.toString())

            val exerciseSetsJson = JSONArray()
            for (exerciseSet in metricDataGrid) {
                val exerciseSetJson = JSONArray()

                for (metricValue in exerciseSet) {
                    exerciseSetJson.put(metricValue.metricValID)
                }

                exerciseSetsJson.put(exerciseSet)
            }
            outputJson.put("ExerciseSets", exerciseSetsJson.toString())

            outputJson.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
                "Error converting ExerciseStats $eStatsID to json string!"
            }
    }

    companion object {
        /**
         * Creates an exercise based on the parsed jsonString obtained
         * from ExerciseStats.toJsonString, mainly for usage with DataStore
         */
        fun fromJsonString(jsonString: String,
                           exerciseList: List<Exercise>,
                           valueList: List<ExerciseStatValue>): ExerciseStats {
            val jsonObject = JSONObject(jsonString)

            // Retrieve related exercise and workout from relevant lists
            val exerciseID = jsonObject.get("Exercise").toString()
            val exercise = exerciseList.first {e -> e.exerciseID == UUID.fromString(exerciseID)}
            val workoutID = UUID.fromString(jsonObject.get("Workout").toString())

            // Create the output ExerciseStats
            val output = ExerciseStats(exercise, workoutID,
                UUID.fromString(jsonObject.get("UniqueID").toString()))

            // Fill out the ExerciseStats sets
            val exerciseSets = JSONArray(jsonObject.get("ExerciseSets").toString())
            for (i in 0..< exerciseSets.length()) {
                val jsonArray = JSONArray(exerciseSets[i]) // get each set json
                val newSet = ArrayList<ExerciseStatValue>() // array of set's metricValues

                // get metricValues from DataStore's list using ID, and add to the exercise set
                for (i in 0..< jsonArray.length()) {
                    val metricValueID = jsonArray.get(i).toString()
                    val metricValue = valueList.first {v ->
                        v.metricValID == UUID.fromString(metricValueID)}

                    newSet.add(metricValue)
                }

                // add the set to the output's array of sets
                output.addSet(newSet)
            }

            return output
        }
    }
}