package com.example.workoutapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise

class RegimenDesignActivity : AppCompatActivity(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener{

    // The list of data that is displayed by recyclerView
    private var displayList = ArrayList<Exercise>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_regimen_design)

        // Setting up the recyclerView to display exercises in this regimen
        recyclerView = findViewById(R.id.rv_regimenExerciseList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Reusing ExerciseManagementAdapter for simplicity
        recyclerView.adapter = ExerciseManagementAdapter(displayList, this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_design)
    }

    override fun onListItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}