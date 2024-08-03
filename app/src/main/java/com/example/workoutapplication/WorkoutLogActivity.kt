package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import org.json.JSONArray

class WorkoutLogActivity : AppCompatActivity() {

    private lateinit var regimenList: ArrayList<Regimen>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_log)

        val dataStoreSingleton = DataStoreSingleton.getInstance(this)
        val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

        regimenList = getRegimenListFromDataStore(dataStoreHelper)
        val spinnerRegimenList = regimenList.map {regimen -> regimen.name!!}
        val regimenSpinner = findViewById<Spinner>(R.id.spin_workoutLogRegimen)

       ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerRegimenList)
           .also { adapter ->
               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
               regimenSpinner.adapter = adapter
           }
    }

    private fun getRegimenListFromDataStore(dataStoreHelper: DataStoreHelper): ArrayList<Regimen> {
        val regimenList = ArrayList<Regimen>()

        // get the exercise list
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList")
        val jsonExerciseArray = if (exerciseListJson != null) {
            JSONArray(exerciseListJson)
        } else JSONArray()

        val exerciseList = ArrayList<Exercise>()
        for (i in 0..< jsonExerciseArray.length()) {
            val exercise = Exercise.fromJsonString(jsonExerciseArray[i].toString())
            exerciseList.add(exercise)
        }

        // Get the regimens from datastore
        val regimenListJson: String? = dataStoreHelper.getStringValue("RegimenList")
        val jsonRegimenArray = if (regimenListJson != null) {
            JSONArray(regimenListJson)
        } else JSONArray() // no preference exists yet for regimens

        Log.d("JSONArray", regimenListJson.toString())

        // Setting up Activity's list of exercises
        for (i in 0 ..< jsonRegimenArray.length()) {
            val regimenDataStore = RegimenDataStore.fromJsonString(jsonRegimenArray[i].toString())
            val regimen = Regimen(regimenDataStore, exerciseList)

            regimenList.add(regimen)
        }

        return regimenList
    }
}