package com.example.workoutapplication.dataClasses

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.LinkedList
import java.util.UUID

/**
 * Helper data class to make retrieving regimens from DataStore simpler
 *
 * Instead of holding a collection of Exercises like Regimen, this class
 * instead holds a collection of Strings, which correlate to the UUIDs
 * of the relevant exercises
 */
class RegimenDataStore() {

    var regimenID: String? = null
    var name: String? = null
    var description: String? = null
    var exerciseIDList = LinkedList<String>()


    companion object {
        /**
         * Use the data from a json string to create a RegimenDataStore
         */
        fun fromJsonString(jsonString: String?): RegimenDataStore {
            val outputRegimen = RegimenDataStore()
            try {
                val jsonRegimen = JSONObject(jsonString)
                outputRegimen.apply() {
                    regimenID = jsonRegimen.get("UniqueID").toString()
                    name = jsonRegimen.get("Name").toString()
                    description = jsonRegimen.get("Description").toString()

                    val exerciseIDArray = JSONArray(jsonRegimen.get("ExerciseIDs").toString())
                    for (i in 0 until exerciseIDArray.length()) {
                        exerciseIDList.add(exerciseIDArray[i].toString())
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return outputRegimen
        }
    }
}