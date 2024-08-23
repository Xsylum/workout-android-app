package com.example.workoutapplication

import android.app.Activity
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric
import com.example.workoutapplication.dataClasses.ExerciseStatValue
import com.example.workoutapplication.dataClasses.ExerciseStats
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import com.example.workoutapplication.dataClasses.WorkoutLog
import com.example.workoutapplication.dataClasses.WorkoutLogDataStore
import org.json.JSONArray



enum class DataStoreKey {
    EXERCISE_METRIC("ExerciseMetricList"),
    EXERCISE_STAT_VALUE("ExerciseStatValuesList"),
    EXERCISE("ExerciseList"),
    REGIMEN("RegimenList"),
    EXERCISE_STAT("ExerciseStatsList"),
    WORKOUT("WorkoutLogList");

    val key: String
    constructor(key: String) {
        this.key = key
    }
}

/**
 * NOTE TO USERS: Always try and call the highest-level of required list (requires the most other
 * lists to build its objects), as it may reduce the necessary calls to DataStore
 */

fun getDataStoreHelper(context: Activity): DataStoreHelper {
    val dataStoreSingleton = DataStoreSingleton.getInstance(context)
    return DataStoreHelper(context, dataStoreSingleton.dataStore!!)
}

fun getDataStoreObjectList(dsKey: DataStoreKey, dataStoreHelper: DataStoreHelper)
: ArrayList<Any> {
    val outputArray = ArrayList<Any>()

    // Retrieving the json-list for the target key from the database
    val targetJsonList: String? = dataStoreHelper.getStringValue(dsKey.key)
    val jsonObjectArray = if (targetJsonList != null) {
        JSONArray(targetJsonList)
    } else JSONArray()

    // performing the relevant object conversions based on key
    when (dsKey) {
        DataStoreKey.EXERCISE_METRIC -> {
            // turn the jsonString metrics into a list of ExerciseMetric objects
            for (i in 0..< jsonObjectArray.length()) {
                val metric = ExerciseMetric.fromJsonString(jsonObjectArray[i].toString())
                outputArray.add(metric)
            }
        }

        DataStoreKey.EXERCISE_STAT_VALUE -> {
            val metrics = getDataStoreObjectList(DataStoreKey.EXERCISE_METRIC, dataStoreHelper)
                    as ArrayList<ExerciseMetric>

            for (i in 0..<jsonObjectArray.length()) {
                val statValue =
                    ExerciseStatValue.fromJsonString(jsonObjectArray.get(i).toString(), metrics)
                outputArray.add(statValue)
            }
        }

        DataStoreKey.EXERCISE -> {
            val metrics = getDataStoreObjectList(DataStoreKey.EXERCISE_METRIC, dataStoreHelper)
                    as ArrayList<ExerciseMetric>

            for (i in 0..<jsonObjectArray.length()) {
                val statValue = Exercise.fromJsonString(jsonObjectArray.get(i).toString(), metrics)
                outputArray.add(statValue)
            }
        }

        DataStoreKey.REGIMEN -> {
            val exercises = getDataStoreObjectList(DataStoreKey.EXERCISE, dataStoreHelper)
                as ArrayList<Exercise>

            for (i in 0 ..< jsonObjectArray.length()) {
                val regimenDataStore = RegimenDataStore.fromJsonString(
                    jsonObjectArray[i].toString()
                )
                val regimen = Regimen(regimenDataStore, exercises)

                outputArray.add(regimen)
            }
        }

        DataStoreKey.EXERCISE_STAT -> {
            //TODO restructure either this or Exercise/ExerciseStatValue to avoid
            //     getting metrics from dataStore in both
            val exercises = getDataStoreObjectList(DataStoreKey.EXERCISE, dataStoreHelper)
                    as ArrayList<Exercise>
            val exerciseStatValues = getDataStoreObjectList(
                DataStoreKey.EXERCISE_STAT_VALUE,
                    dataStoreHelper) as ArrayList<ExerciseStatValue>

            for (i in 0..<jsonObjectArray.length()) {
                val exerciseStat = ExerciseStats.fromJsonString(
                    jsonObjectArray.get(i).toString(),
                    exercises, exerciseStatValues
                )
                outputArray.add(exerciseStat)
            }
        }

        DataStoreKey.WORKOUT -> {
            //TODO restructure either this or Regimen/ExerciseStat to avoid
            //     getting exercises from dataStore in both
            val regimens = getDataStoreObjectList(DataStoreKey.REGIMEN, dataStoreHelper)
                    as ArrayList<Regimen>
            val exerciseStats = getDataStoreObjectList(DataStoreKey.EXERCISE_STAT, dataStoreHelper)
                    as ArrayList<ExerciseStats>

            for (i in 0..<jsonObjectArray.length()) {
                val workoutLogDS = WorkoutLogDataStore.fromJsonString(
                    jsonObjectArray.get(i).toString()
                )
                val workoutLog = WorkoutLog(workoutLogDS, regimens, exerciseStats)
                outputArray.add(workoutLog)
            }
        }
    }

    return outputArray
}

//TODO create setDataStoreKeyValue helper method