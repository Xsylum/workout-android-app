package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise

class RegimenDesignAddExercisesFragment(val userExercises: ArrayList<Exercise>): DialogFragment(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener {

    val regimenAdditions = ArrayList<Exercise>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            // Inflating and setting the layout for this dialog
            val layoutView = inflater.inflate(R.layout.fragment_regimen_design_exercises, null)
            builder.setView(layoutView)

            val userExercisesRV = layoutView
                .findViewById<RecyclerView>(R.id.rv_regimenAvailableExercises)

            //TODO figure out how to add recyclerview to a fragment
            userExercisesRV.layoutManager = LinearLayoutManager(requireContext()) // it????
            userExercisesRV.adapter = ExerciseManagementAdapter(userExercises, this)

            builder.setPositiveButton("Confirm") { dialog, id ->

            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onListItemClick(position: Int) {
        Log.d("RegimenDesignTest", "Exercise ${userExercises[position].exerciseID} "
                + "is at position $position")
    }

}