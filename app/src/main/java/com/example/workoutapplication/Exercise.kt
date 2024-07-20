package com.example.workoutapplication

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.LinkedList
import java.util.UUID

class Exercise {
    // TODO: check uniqueness (unique name/description?)
    var exerciseID: UUID? = null // Class with very low-probably of generating the same "unique ID"
        private set
    @JvmField
    var name: String? = null
    @JvmField
    var description: String? = null
    @JvmField
    var thumbnailID: String? = null // Passing the storing/loading of images onto the activity

    // Tag Methods
    var tags // maybe switch this to pairs or a Tag class (tag name, reference to Tag)
            : LinkedList<String?>
        private set

    private constructor() {
        tags = LinkedList()
    }

    // Constructor for the creation of "new" exercises, with a name and description as the base
    constructor(name: String?, description: String?) {
        exerciseID = UUID.randomUUID()
        this.name = name
        this.description = description
        tags = LinkedList()
    }

    constructor(uniqueID: String?, name: String?, description: String?) {
        exerciseID = UUID.fromString(uniqueID)
        this.name = name
        this.description = description
        tags = LinkedList()
    }

    // Private setter, intended for use in .fromJsonString() method
    private fun setExerciseID(exerciseID: UUID) {
        this.exerciseID = exerciseID
    }

    fun clearAllTags() {
        tags.clear()
    }

    fun removeTag(tag: String?) {
        tags.remove(tag)
    }

    fun addTag(tag: String?) {
        tags.add(tag)
    }

    // JSON Methods
    // Returns a Json string representation of the current object
    fun toJsonString(): String {
        // TODO: for boiler-plate reasons, maybe try and remove JSONObject and manually convert to strings --> no dependence on org.json
        val jsonExercise = JSONObject()
        try {
            // Putting exercise's variable values
            jsonExercise.put("UniqueID", exerciseID.toString())
            jsonExercise.put("Name", name)
            jsonExercise.put("Description", description)
            jsonExercise.put("ThumbnailID", thumbnailID)

            // JSONArray to hold exercise's tags
            val array = JSONArray(tags)
            jsonExercise.put("Tags", array)
        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for $exerciseID"
        }
        return jsonExercise.toString()
    }

    companion object {
        /**
         * Exercise constructor which takes a JsonString, in the format of .toJsonString()
         * @param JsonString
         * @return an Exercise with attributes based on JsonString
         */
        fun fromJsonString(jsonString: String?): Exercise {
            val resultExercise = Exercise()
            try {
                val jsonObject = JSONObject(jsonString)
                resultExercise.apply() {
                    val IDString = jsonObject["UniqueID"] as String
                    exerciseID = UUID.fromString(IDString)
                    name = jsonObject["Name"].toString()
                    description = jsonObject["Description"].toString()
                    thumbnailID = jsonObject["ThumbnailID"].toString()

                    val tagsArray = JSONArray(jsonObject["Tags"].toString())
                    for (i in 0 until tagsArray.length()) {
                        resultExercise.addTag(tagsArray[i].toString())
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return resultExercise
        }
    }
}