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

    private lateinit var regimenID: UUID
    var name: String? = null
    var description: String? = null
    private var exerciseList = LinkedList<Exercise>(); //TODO("hold exerciseIDs, call for exercises when needed")

    private constructor() {
        exerciseList = LinkedList<Exercise>()
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
            return "Json conversion error for $regimenID"
        }
        return jsonRegimen.toString()
    }

    companion object {

        fun fromJsonString(jsonString: String?): Regimen {
            //TODO("Not implemented")
            throw NotImplementedError("Not yet Implemented!")
        }
    }
}