package com.example.workoutapplication

import android.app.Activity
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric
import com.example.workoutapplication.dataClasses.ExerciseStatValue
import com.example.workoutapplication.dataClasses.ExerciseStats
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import com.example.workoutapplication.dataClasses.WorkoutEvent
import com.example.workoutapplication.dataClasses.WorkoutEventDataStore
import com.example.workoutapplication.dataClasses.WorkoutLog
import com.example.workoutapplication.dataClasses.WorkoutLogDataStore
import org.json.JSONArray



enum class DataStoreKey(val key: String) {
    EXERCISE_METRIC("ExerciseMetricList"),
    EXERCISE_STAT_VALUE("ExerciseStatValuesList"),
    EXERCISE("ExerciseList"),
    REGIMEN("RegimenList"),
    EXERCISE_STAT("ExerciseStatsList"),
    WORKOUT("WorkoutLogList"),
    WORKOUT_EVENT("WorkoutEventList");
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

        DataStoreKey.WORKOUT_EVENT -> {
            val workouts = getDataStoreObjectList(DataStoreKey.WORKOUT, dataStoreHelper)
                    as ArrayList<WorkoutLog>

            for (i in 0..<jsonObjectArray.length()) {
                val workoutEventDS = WorkoutEventDataStore.fromJsonString(
                    jsonObjectArray.get(i).toString()
                )
                val workoutEvent = WorkoutEvent(workoutEventDS, workouts)
                outputArray.add(workoutEvent)
            }
        }
    }

    return outputArray
}

/**
 * newObject should be the object being added to the DataStore list (i.e. NOT a jsonString, etc.)
 *
 * Returns the DataStore list position that the data was successfully stored at
 */
fun setDataStoreAtListPosition(dsKey: DataStoreKey, position: Int,
                               newObject: Any, dataStoreHelper: DataStoreHelper): Int {
    val targetList: String? = dataStoreHelper.getStringValue(dsKey.key)
    val targetJsonArray = if (targetList != null) {
        JSONArray(targetList)
    } else JSONArray()

    // interpreting newObject as relevant type to get its jsonString representation
    val newObjectJsonString: String = when(dsKey) {
        DataStoreKey.EXERCISE_METRIC -> (newObject as ExerciseMetric).toJsonString()
        DataStoreKey.EXERCISE_STAT_VALUE -> (newObject as ExerciseStatValue).toJsonString()
        DataStoreKey.EXERCISE -> (newObject as Exercise).toJsonString()
        DataStoreKey.REGIMEN -> (newObject as Regimen).toJsonString()
        DataStoreKey.EXERCISE_STAT -> (newObject as ExerciseStats).toJsonString()
        DataStoreKey.WORKOUT -> (newObject as WorkoutLog).toJsonString()
        DataStoreKey.WORKOUT_EVENT -> (newObject as WorkoutEvent).toJsonString()
    }

    if (position == -1 || position >= targetJsonArray.length()) { // new object
        targetJsonArray.put(newObjectJsonString)
    } else { // editing existing object
        targetJsonArray.put(position, newObjectJsonString)
    }

    dataStoreHelper.setStringValue(dsKey.key, targetJsonArray.toString())
    //TODO need to build testcases to ensure covers correct range
    return if (position == -1 || position >= targetJsonArray.length() - 1)
            (targetJsonArray.length() - 1) else position
}