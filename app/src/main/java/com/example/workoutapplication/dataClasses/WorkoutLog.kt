package com.example.workoutapplication.dataClasses

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.IllegalStateException
import java.time.LocalTime
import java.util.UUID

class WorkoutLog {

    //TODO might need a connection to WorkoutEvent, will cause circular data loading though (maybe only hold eventID?)
    var workoutID: UUID
    var timeOfCompletion: LocalTime? = null
        private set
    var workoutRegimen: Regimen? = null
    val exerciseStats = ArrayList<ExerciseStats>()

    constructor() {
        workoutID = UUID.randomUUID()
    }

    constructor(workoutDataStore: WorkoutLogDataStore, regimenList: List<Regimen>,
                eStatList: List<ExerciseStats>) {
        workoutID = UUID.fromString(workoutDataStore.workoutID)
        timeOfCompletion = if (workoutDataStore.timeOfCompletion == "null") {
            null
        } else {
            LocalTime.parse(workoutDataStore.timeOfCompletion)
        }
        if (workoutDataStore.regimenID == "null") {
            workoutRegimen = null

        } else {
            workoutRegimen = regimenList.first {r ->
                r.regimenID == UUID.fromString(workoutDataStore.regimenID)}
            for (eStatIDString in workoutDataStore.exerciseStatIDs) {
                val targetExerciseStat = eStatList.firstOrNull {eS ->
                    eS.eStatsID == UUID.fromString(eStatIDString)}
                if (targetExerciseStat != null) exerciseStats.add(targetExerciseStat)
            }
        }
    }

    /**
     * Clears existing ExerciseStats from this WorkoutLog and fills it with new
     * ExerciseStats based on the exercises in workoutRegimen
     *
     * NOTE: If this method is called when workoutRegimen is null, it
     * will clear the workout's existing exercise stats
     */
    fun replaceExerciseStatsForNewRegimen() {
        exerciseStats.clear()

        if (workoutRegimen != null) {
            val exerciseList = workoutRegimen!!.exerciseList
            for (exercise in exerciseList) {
                val newExerciseStat = ExerciseStats(exercise, this.workoutID)
                exerciseStats.add(newExerciseStat)
                newExerciseStat.addEmptySet()
                newExerciseStat.addEmptySet()
            }
            Log.d("Test", exerciseStats.toString())
        }
    }

    fun toJsonString(): String {
        val output = JSONObject()

        try {
            output.put("UniqueID", workoutID)
            output.put("RegimenID", workoutRegimen?.regimenID ?: "null")
            output.put("TimeOfCompletion", timeOfCompletion.toString())

            val exerciseStatIDs = JSONArray()
            for (exerciseStat in exerciseStats) {
                exerciseStatIDs.put(exerciseStat.eStatsID.toString())
            }
            output.put("ExerciseStatIDs", exerciseStatIDs)
        } catch (e: JSONException) {
            e.printStackTrace()
            return "error converting WorkoutLog $workoutID to json!"
        }

        return output.toString()
    }

}

class WorkoutLogDataStore private constructor() {

    var workoutID = ""
    var regimenID = ""
    var timeOfCompletion = ""
    var exerciseStatIDs = ArrayList<String>()

    companion object {
        fun fromJsonString(jsonString: String): WorkoutLogDataStore {
            val output = WorkoutLogDataStore()

            try {
                val jsonObject = JSONObject(jsonString)

                output.workoutID = jsonObject.get("UniqueID").toString()
                output.regimenID = jsonObject.get("RegimenID").toString()
                output.timeOfCompletion = jsonObject.get("TimeOfCompletion").toString()

                val jsonExerciseStatIDs = JSONArray(jsonObject.get("ExerciseStatIDs").toString())

                for (i in 0..<jsonExerciseStatIDs.length()) {
                    output.exerciseStatIDs.add(jsonExerciseStatIDs.get(i).toString())
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return output
        }
    }

}