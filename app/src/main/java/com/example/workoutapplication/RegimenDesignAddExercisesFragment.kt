package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise

class RegimenDesignAddExercisesFragment(private val userExercises: ArrayList<Exercise>): DialogFragment(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener {

    val addedExercises = ArrayList<Exercise>()

    private lateinit var userExercisesRV: RecyclerView
    private lateinit var addedExercisesRV: RecyclerView

    private lateinit var listener: AddExercisesListener

    /**
     * Listener interface to pass back the list of newly added exercises
     */
    interface AddExercisesListener {
        fun onExerciseAddPositiveClick(dialog: DialogFragment)
    }

    /**
     * Ensures that the context this dialog is attached to implements AddExercisesListener
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as AddExercisesListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement AddExercisesListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            // Inflating and setting the layout for this dialog
            val layoutView = inflater.inflate(R.layout.fragment_regimen_design_exercises, null)
            builder.setView(layoutView)

            userExercisesRV = layoutView.findViewById<RecyclerView>(R.id.rv_regimenAvailableExercises)
            addedExercisesRV = layoutView
                .findViewById<RecyclerView>(R.id.rv_regimenNewExercises)

            userExercisesRV.layoutManager = LinearLayoutManager(requireContext()) // it????
            userExercisesRV.adapter = ExerciseManagementAdapter(userExercises, this)

            addedExercisesRV.layoutManager = LinearLayoutManager(requireContext())
            addedExercisesRV.adapter = ExerciseManagementAdapter(addedExercises, this)

            builder.setPositiveButton("Confirm") { dialog, id ->
                listener.onExerciseAddPositiveClick(this)
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /**
     * Swaps an exercise between userExercises and addedExercises when clicked in RecyclerView
     */
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

    /**
     * Moves an exercise from userExercises list to the addedExercises list
     */
    private fun addToNewExercises(position: Int) {
        val newExercise = userExercises.removeAt(position)
        addedExercises.add(newExercise)
    }

    /**
     * Moves an exercise from addedExercises list back to the userExercises list
     */
    private fun removeFromNewExercises(position: Int) {
        val oldExercise = addedExercises.removeAt(position)
        userExercises.add(oldExercise)
    }

    /**
     * Updates the two RecyclerViews when an exercise is swapped to addedExercises
     */
    private fun updateRecyclerViewsAddExercise(position: Int) {
        userExercisesRV.adapter!!.notifyItemRemoved(position)
        addedExercisesRV.adapter!!.notifyItemInserted(addedExercises.size - 1)
    }

    /**
     * Updates the two RecyclerViews when an exercise is swapped to userExercises
     */
    private fun updateRecyclerViewsRemoveExercise(position: Int) {
        addedExercisesRV.adapter!!.notifyItemRemoved(position)
        userExercisesRV.adapter!!.notifyItemInserted(userExercises.size - 1)
    }

}