package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class RegimenRemoveExerciseFragment(private val exerciseName: String,
                                    private val position: Int): DialogFragment() {

    private lateinit var listener: RemoveExerciseListener

    interface RemoveExerciseListener {
        fun onRemoveExercisePositiveClick(dialog: DialogFragment, position: Int)
    }

    /**
     * Ensure that any activity/context attaching this dialogFragment
     * implements RemoveExerciseListener, so as to handle its listening methods
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as RemoveExerciseListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context does not implement RemoveExerciseListener!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage("Are you sure you want to delete $exerciseName")
                .setPositiveButton("Confirm") { dialog, id ->
                    listener.onRemoveExercisePositiveClick(this, position)
                }
                .setNegativeButton("Cancel") {dialog, id ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}