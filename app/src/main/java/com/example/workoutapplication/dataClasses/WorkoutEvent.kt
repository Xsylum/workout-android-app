package com.example.workoutapplication.dataClasses

import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.util.UUID

class WorkoutEvent {

    enum class EventDotColour(val hexColour: String) {
        BLACK("#000000"),
        RED("#FF0000"),
        ORANGE("#FF9D00"),
        YELLOW("#FFFF00"),
        GREEN("#00FF00"),
        BLUE("#0000FF"),
        PURPLE("#AA00FF");
    }

    var eventID: UUID
    var workoutLog: WorkoutLog
    var date: LocalDate
    var dotColour: EventDotColour // TODO maybe just change this to a hex colour?
    var altDisplayName: String = ""
    var altDisplayDesc: String = ""

    // Creating a WorkoutEvent from scratch
    constructor(workout: WorkoutLog, date: LocalDate,
                dotColour: EventDotColour = EventDotColour.BLUE,
                eventID: UUID = UUID.randomUUID(), altDisplayName: String = "",
                altDisplayDesc: String = "") {
        this.eventID = eventID
        this.workoutLog = workout
        this.date = date
        this.dotColour = dotColour
        this.altDisplayName = altDisplayName
        this.altDisplayDesc = altDisplayDesc
    }

    // Pulling WorkoutEvents from DataStore, and matching them to the relevant workout object
    constructor(workoutEventDS: WorkoutEventDataStore, workoutList: List<WorkoutLog>) {
        // Converting values out from workoutEventDS
        val eventID = UUID.fromString(workoutEventDS.eventIDString)
        val workout = workoutList.first {
            w -> w.workoutID == UUID.fromString(workoutEventDS.workoutIDString)
        }
        val date = LocalDate.parse(workoutEventDS.dateString)
        val dotColour = when (workoutEventDS.dotColourString) {
            "RED" -> EventDotColour.RED
            "ORANGE" -> EventDotColour.ORANGE
            "YELLOW" -> EventDotColour.YELLOW
            "GREEN" -> EventDotColour.GREEN
            "BLUE" -> EventDotColour.BLUE
            "PURPLE" -> EventDotColour.PURPLE
            else -> EventDotColour.BLACK
        }

        // Assigning values to this object being constructed
        this.eventID = eventID
        this.workoutLog = workout
        this.date = date
        this.dotColour = dotColour
        this.altDisplayName = workoutEventDS.altDisplayName
        this.altDisplayDesc = workoutEventDS.altDisplayDesc
    }

    fun toJsonString(): String {
        val outputJson = JSONObject()

        try {
            outputJson.put("EventID", eventID.toString())
            outputJson.put("WorkoutID", workoutLog.workoutID.toString())
            outputJson.put("Date", date.toString())
            outputJson.put("DotColour", when (dotColour) {
                EventDotColour.BLACK -> "BLACK"
                EventDotColour.RED -> "RED"
                EventDotColour.ORANGE -> "ORANGE"
                EventDotColour.YELLOW -> "YELLOW"
                EventDotColour.GREEN -> "GREEN"
                EventDotColour.BLUE -> "BLUE"
                EventDotColour.PURPLE -> "PURPLE"
            })
            outputJson.put("AltName", altDisplayName)
            outputJson.put("AltDesc", altDisplayDesc)
        } catch (e: JSONException) {
            e.printStackTrace()
            return "Json conversion error for WorkoutEvent $eventID"
        }

        return outputJson.toString()
    }
}

class WorkoutEventDataStore private constructor() {

    var eventIDString = ""
    var workoutIDString = ""
    var dateString = ""
    var dotColourString = ""
    var altDisplayName = ""
    var altDisplayDesc = ""

    companion object {
        fun fromJsonString(jsonString: String): WorkoutEventDataStore {
            val output = WorkoutEventDataStore()

            try {
                val jsonObject = JSONObject(jsonString)

                output.eventIDString = jsonObject.get("EventID").toString()
                output.workoutIDString = jsonObject.get("WorkoutID").toString()
                output.dateString = jsonObject.get("Date").toString()
                output.dotColourString = jsonObject.get("DotColour").toString()
                output.altDisplayName = jsonObject.get("AltName").toString()
                output.altDisplayDesc = jsonObject.get("AltDesc").toString()

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return output
        }
    }

}