package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise

class RegimenDesignAddExercisesFragment(private val userExercises: ArrayList<Exercise>): DialogFragment(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener {

    private val addedExercises = ArrayList<Exercise>()

    private lateinit var userExercisesRV: RecyclerView
    private lateinit var addedExercisesRV: RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            addedExercises.add(Exercise("test", "test"))

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            // Inflating and setting the layout for this dialog
            val layoutView = inflater.inflate(R.layout.fragment_regimen_design_exercises, null)
            builder.setView(layoutView)

            userExercisesRV = layoutView.findViewById<RecyclerView>(R.id.rv_regimenAvailableExercises)
            addedExercisesRV = layoutView
                .findViewById<RecyclerView>(R.id.rv_regimenNewExercises)

            //TODO figure out how to add recyclerview to a fragment
            userExercisesRV.layoutManager = LinearLayoutManager(requireContext()) // it????
            userExercisesRV.adapter = ExerciseManagementAdapter(userExercises, this)

            addedExercisesRV.layoutManager = LinearLayoutManager(requireContext())
            addedExercisesRV.adapter = ExerciseManagementAdapter(addedExercises, this)

            builder.setPositiveButton("Confirm") { dialog, id ->

            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onListItemClick(adapter: ExerciseManagementAdapter, position: Int) {
        if (userExercisesRV.adapter == adapter) {
            Log.d("RegimenDesignTest", "Exercise ${userExercises[position].name} "
                    + "is at position $position")
            addToNewExercises(position)
            updateRecyclerViewsAddExercise(position)
        } else {
            Log.d("RegimenDesignTest", "Exercise ${addedExercises[position].name} "
                    + "is at position $position")
            removeFromNewExercises(position)
            updateRecyclerViewsRemoveExercise(position)
        }
    }

    private fun addToNewExercises(position: Int) {
        val newExercise = userExercises.removeAt(position)
        addedExercises.add(newExercise)
    }

    private fun removeFromNewExercises(position: Int) {
        val oldExercise = addedExercises.removeAt(position)
        userExercises.add(oldExercise)
    }

    private fun updateRecyclerViewsAddExercise(position: Int) {
        userExercisesRV.adapter!!.notifyItemRemoved(position)
        addedExercisesRV.adapter!!.notifyItemInserted(addedExercises.size - 1)
    }

    private fun updateRecyclerViewsRemoveExercise(position: Int) {
        addedExercisesRV.adapter!!.notifyItemRemoved(position)
        userExercisesRV.adapter!!.notifyItemInserted(userExercises.size - 1)
    }

}