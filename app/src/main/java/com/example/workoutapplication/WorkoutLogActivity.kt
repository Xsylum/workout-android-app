package com.example.workoutapplication

import android.content.Intent
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
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.WorkoutEvent
import com.example.workoutapplication.dataClasses.WorkoutLog

class WorkoutLogActivity : AppCompatActivity() {

    private lateinit var regimenList: ArrayList<Regimen>
    private lateinit var workout: WorkoutLog

    // DataStore
    val dataStoreHelper = getDataStoreHelper(this)

    var workoutDataStorePosition = -1

    private lateinit var exerciseStatsRV: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_log)

        // Regimen Spinner Setup
        regimenList = getDataStoreObjectList(DataStoreKey.REGIMEN, dataStoreHelper)
                as ArrayList<Regimen>
        setupRegimenSpinner(regimenList)

        // Setup activity views for the current intent (new WorkoutLog vs Editing WorkoutLog)
        workoutDataStorePosition = intent.getIntExtra("WorkoutDSPosition", -1)
        workout = if (workoutDataStorePosition != -1) {
            // User is editing an existing WorkoutLog (get WorkoutList over building
            // specific workout object, which requires ExerciseStatLists)
            val workoutList = getDataStoreObjectList(DataStoreKey.WORKOUT, dataStoreHelper)
                    as ArrayList<WorkoutLog>
            workoutList[workoutDataStorePosition]
        } else {
            // User is designing a new WorkoutLog
            WorkoutLog()
        }

        // ExerciseStat List setup
        exerciseStatsRV = findViewById<RecyclerView>(R.id.rv_workoutExerciseStats)
        exerciseStatsRV.layoutManager = LinearLayoutManager(this)
        exerciseStatsRV.adapter = ExerciseStatAdapter(workout.exerciseStats)
    }

    private fun setupRegimenSpinner(regimenList: ArrayList<Regimen>) {
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

                saveWorkoutToDataStore()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun saveWorkoutToDataStore() {
        workoutDataStorePosition = setDataStoreAtListPosition(DataStoreKey.WORKOUT,
            workoutDataStorePosition, workout, dataStoreHelper)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent()
        intent.putExtra("WORKOUT_POSITION", workoutDataStorePosition)
        setResult(0, intent)
        finish()
    }
}