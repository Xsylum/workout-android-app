package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric
import com.example.workoutapplication.dataClasses.ExerciseStats
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import com.example.workoutapplication.dataClasses.WorkoutLog
import org.json.JSONArray
import java.util.LinkedList

class WorkoutLogActivity : AppCompatActivity() {

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
        val spinnerRegimenList = regimenList.map {regimen -> regimen.name!!} as ArrayList<String>
            spinnerRegimenList.add(0, "Please Select a Regimen")

        val regimenSpinner = findViewById<Spinner>(R.id.spin_workoutLogRegimen)
        val regimenSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerRegimenList)
        regimenSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regimenSpinner.adapter = regimenSpinnerAdapter

        regimenSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) { workout.workoutRegimen = null }
                    else { workout.workoutRegimen = regimenList[position - 1] }

                // TODO currently adding temp exercise sets in below method, include a button to add them dynamically!
                workout.replaceExerciseStatsForNewRegimen()
                exerciseStatsRV.adapter!!.notifyDataSetChanged()

                for (exerciseStat in workout.exerciseStats) {
                    Log.d("Testing", exerciseStat.metricDataGrid.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        // ExerciseStat List setup
        exerciseStatsRV = findViewById<RecyclerView>(R.id.rv_workoutExerciseStats)
        exerciseStatsRV.layoutManager = LinearLayoutManager(this)
        exerciseStatsRV.adapter = ExerciseStatAdapter(workout.exerciseStats)
    }

    private fun getRegimenListFromDataStore(dataStoreHelper: DataStoreHelper): ArrayList<Regimen> {
        val regimenList = ArrayList<Regimen>()

        // Retrieving user metric list from DataStore
        val metricListJson: String? = dataStoreHelper.getStringValue("ExerciseMetricList")
        val jsonMetricArray = if (metricListJson != null) {
            JSONArray(metricListJson)
        } else JSONArray()

        // turn the jsonString metrics into a list of ExerciseMetric objects
        val userMetricArray = ArrayList<ExerciseMetric>()
        for (i in 0..< jsonMetricArray.length()) {
            val metric = ExerciseMetric.fromJsonString(jsonMetricArray[i].toString())
            userMetricArray.add(metric)
        }

        // get the exercise list
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList")
        val jsonExerciseArray = if (exerciseListJson != null) {
            JSONArray(exerciseListJson)
        } else JSONArray()

        val exerciseList = ArrayList<Exercise>()
        for (i in 0..< jsonExerciseArray.length()) {
            val exercise = Exercise.fromJsonString(jsonExerciseArray[i].toString(), userMetricArray)
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