package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseStats
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import com.example.workoutapplication.dataClasses.WorkoutLog
import org.json.JSONArray

class WorkoutLogActivity : AppCompatActivity(),
    AdapterView.OnItemSelectedListener {

    private lateinit var regimenList: ArrayList<Regimen>
    private var workout = WorkoutLog()

    private lateinit var exerciseStatsRV: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_log)

        // DataStore
        val dataStoreSingleton = DataStoreSingleton.getInstance(this)
        val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

        // Regimen Spinner Setup
        regimenList = getRegimenListFromDataStore(dataStoreHelper)
        val spinnerRegimenList = regimenList.map {regimen -> regimen.name!!}
        val regimenSpinner = findViewById<Spinner>(R.id.spin_workoutLogRegimen)

        ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerRegimenList)
           .also { adapter ->
               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
               regimenSpinner.adapter = adapter
           }

        // ExerciseStat List setup
        exerciseStatsRV = findViewById<RecyclerView>(R.id.rv_workoutExerciseStats)
        exerciseStatsRV.layoutManager = LinearLayoutManager(this)
        exerciseStatsRV.adapter = ExerciseStatAdapter(workout.exerciseStats)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        workout.workoutRegimen = regimenList[position]
        exerciseStatsRV.adapter!!.notifyDataSetChanged()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

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