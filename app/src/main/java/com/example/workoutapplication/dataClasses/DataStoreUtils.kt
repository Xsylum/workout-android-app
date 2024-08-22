package com.example.workoutapplication.dataClasses

import android.app.Activity
import com.example.workoutapplication.DataStoreHelper
import com.example.workoutapplication.DataStoreSingleton
import org.json.JSONArray

/**
 * NOTE TO USERS: Always try and call the highest-level of required list (requires the most other
 * lists to build its objects), as it may reduce the necessary calls to DataStore
 */

fun getDataStoreHelper(context: Activity) {
    val dataStoreSingleton = DataStoreSingleton.getInstance(context)
    val dataStoreHelper = DataStoreHelper(context, dataStoreSingleton.dataStore!!)
}

/**
 * get ExerciseMetric
 */
fun getUserMetricsFromDataStore(dataStoreHelper: DataStoreHelper): ArrayList<ExerciseMetric> {
    val output = ArrayList<ExerciseMetric>()

    // Retrieving user metric list from DataStore
    val metricListJson: String? = dataStoreHelper.getStringValue("ExerciseMetricList")
    val jsonMetricArray = if (metricListJson != null) {
        JSONArray(metricListJson)
    } else JSONArray()

    // turn the jsonString metrics into a list of ExerciseMetric objects
    for (i in 0..< jsonMetricArray.length()) {
        val metric = ExerciseMetric.fromJsonString(jsonMetricArray[i].toString())
        output.add(metric)
    }

    return output
}

/**
 * get ExerciseStatValue
 * (requires ExerciseMetric)
 */
fun getExerciseStatValuesFromDataStore(dataStoreHelper: DataStoreHelper,
                                       userMetrics: ArrayList<ExerciseMetric> =
                                           getUserMetricsFromDataStore(dataStoreHelper))
: ArrayList<ExerciseStatValue> {
    val output = ArrayList<ExerciseStatValue>()

    // Exercise metric values
    val statValuesJson: String? = dataStoreHelper.getStringValue("ExerciseStatValuesList")
    val jsonStatValuesArray = if (statValuesJson != null) {
        JSONArray(statValuesJson)
    } else JSONArray()

    for (i in 0..<jsonStatValuesArray.length()) {
        val statValue = ExerciseStatValue.
        fromJsonString(jsonStatValuesArray.get(i).toString(), userMetrics)
        output.add(statValue)
    }

    return output
}



/**
 * get Exercise
 * (requires ExerciseMetric)
 */
fun getExerciseListFromDataStore(dataStoreHelper: DataStoreHelper,
                                 userMetrics: ArrayList<ExerciseMetric> =
                                     getUserMetricsFromDataStore(dataStoreHelper))
: ArrayList<Exercise> {
    val output = ArrayList<Exercise>()

    // Get the JsonString-list of exercises from DataStore
    val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList")
    val jsonExerciseArray = if (exerciseListJson != null) {
        JSONArray(exerciseListJson)
    } else JSONArray()

    // Turn jsonString exercises into list of Exercise objects
    for (i in 0..< jsonExerciseArray.length()) {
        val exercise = Exercise.fromJsonString(jsonExerciseArray[i].toString(), userMetrics)
        output.add(exercise)
    }

    return output
}

/**
 * get Regimen
 * (requires Exercise)
 */
fun getRegimenListFromDataStore(dataStoreHelper: DataStoreHelper,
                               exerciseList: ArrayList<Exercise> =
                                   getExerciseListFromDataStore(dataStoreHelper))
: ArrayList<Regimen> {
    val output = ArrayList<Regimen>()

    // Get the regimens from datastore
    val regimenListJson: String? = dataStoreHelper.getStringValue("RegimenList")
    val jsonRegimenArray = if (regimenListJson != null) {
        JSONArray(regimenListJson)
    } else JSONArray() // no preference exists yet for regimens

    // Setting up Activity's list of exercises
    for (i in 0 ..< jsonRegimenArray.length()) {
        val regimenDataStore = RegimenDataStore.fromJsonString(jsonRegimenArray[i].toString())
        val regimen = Regimen(regimenDataStore, exerciseList)

        output.add(regimen)
    }

    return output
}

/**
 * get ExerciseStats
 * (requires Exercise, ExerciseStatValue)
 */
fun getExerciseStatsFromDataStore(dataStoreHelper: DataStoreHelper): ArrayList<ExerciseStats> {
    val output = ArrayList<ExerciseStats>()
    // speed up retrieval of Exercise and ExerciseStatValue by only calling getUserMetrics once
    val userMetrics = getUserMetricsFromDataStore(dataStoreHelper)
    val exerciseList = getExerciseListFromDataStore(dataStoreHelper, userMetrics)
    val exerciseStatValueList = getExerciseStatValuesFromDataStore(dataStoreHelper, userMetrics)

    // Exercise Stats for this workout
    val exerciseStatsJson: String? = dataStoreHelper.getStringValue("ExerciseStatsList")
    val jsonStatsArray = if (exerciseStatsJson != null) {
        JSONArray(exerciseStatsJson)
    } else JSONArray()

    for (i in 0..<jsonStatsArray.length()) {
        val exerciseStat = ExerciseStats.fromJsonString(jsonStatsArray.get(i).toString(),
            exerciseList, exerciseStatValueList)
        output.add(exerciseStat)
    }

    return output
}

fun getWorkoutLogsFromDataStore(dataStoreHelper: DataStoreHelper): ArrayList<WorkoutLog> {
    val output = ArrayList<WorkoutLog>()
    // TODO speed up below by only having to call getExerciseList once
    val regimenList = getRegimenListFromDataStore(dataStoreHelper)
    val exerciseStatList = getExerciseStatsFromDataStore(dataStoreHelper)

    val workoutLogsJson: String? = dataStoreHelper.getStringValue("WorkoutLogList")
    val jsonWorkoutArray = if (workoutLogsJson != null) {
        JSONArray(workoutLogsJson)
    } else JSONArray()

    for (i in 0..<jsonWorkoutArray.length()) {
        val workoutLogDS = WorkoutLogDataStore.fromJsonString(jsonWorkoutArray.get(i).toString())
        val workoutLog = WorkoutLog(workoutLogDS, regimenList, exerciseStatList)
        output.add(workoutLog)
    }

    return output
}