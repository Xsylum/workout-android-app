package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class UpdateExerciseFragment : DialogFragment() {
    // Defining a custom Dialog container
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage("My Dialog")
                .setPositiveButton("Well Done!") {dialog, id ->
                    // Perform positive button consequences
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // Perform negative button consequences
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}