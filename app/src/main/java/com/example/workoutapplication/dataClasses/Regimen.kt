package com.example.workoutapplication.dataClasses

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.LinkedList
import java.util.UUID

/**
 * A class which allowing the user to define a set of
 * exercises, to act as a plan for the user's workout(s)
 */
class Regimen {

    lateinit var regimenID: UUID
    var name: String? = null
    var description: String? = null
    var exerciseList = ArrayList<Exercise>() //TODO("hold exerciseIDs, call for exercises when needed")
        private set

    private constructor()

    /**
     * Construct a regimen from the simplified RegimenDataStore communication
     * class as well as a list of the user's exercises
     *
     * The constructor will attempt to find RegimenDataStore's UUID Strings
     * in the list of exercises, and will throw an error if these UUIDs are not found
     */
    constructor(regimen: RegimenDataStore, exercises: List<Exercise>) {
        //TODO("Change LinkedList to a hashmap to speed up construction")
        //TODO("Alternatively, keep exercise UUIDs instead, and only find the relevant exercises as needed")

        try {
            regimenID = UUID.fromString(regimen.regimenID)
            name = regimen.name
            description = regimen.description

            for (exerciseID in regimen.exerciseIDList) {
                var exerciseFound = false
                exercises@ for (exercise in exercises) {
                    if (exercise.exerciseID == UUID.fromString(exerciseID)) {
                        exerciseList.add(exercise)
                        exerciseFound = true
                        break@exercises
                    }
                }

                if (!exerciseFound) {
                    throw IllegalArgumentException("Failed to find exercise with string ID" +
                            "$exerciseID for regimen with ID $regimenID")
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    constructor(name: String, description:String) {
        regimenID = UUID.randomUUID()
        this.name = name
        this.description = description
    }

    constructor(uniqueID:String, name:String?, description:String?){
        regimenID = UUID.fromString(uniqueID)
        this.name = name
        this.description = description
    }

    // Modifying Exercise List Methods
    fun addExercise(e: Exercise) {
        exerciseList.add(e)
    }

    fun removeExercise(e: Exercise) {
        exerciseList.remove(e)
    }

    fun removeExercise(index: Int): Exercise {
        return exerciseList.removeAt(index)
    }

    fun swapExerciseOrder(index1: Int, index2: Int) {
        val temp = exerciseList[index2]
        exerciseList[index2] = exerciseList[index1]
        exerciseList[index1] = temp
    }

    fun toJsonString(): String {
        val jsonRegimen = JSONObject()
        try {
            jsonRegimen.put("UniqueID", regimenID.toString())
            jsonRegimen.put("Name", name)
            jsonRegimen.put("Description", description)

            val exerciseIDArray = JSONArray()
            for (e in exerciseList) {
                exerciseIDArray.put(e.exerciseID.toString())
            }
            jsonRegimen.put("ExerciseIDs", exerciseIDArray)

        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for regimen $regimenID"
        }
        return jsonRegimen.toString()
    }
}