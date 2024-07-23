package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class UpdateExerciseFragment : DialogFragment() {
    // Defining a custom Dialog container
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;

            // Inflating/setting the layout for the dialog
            // Parent view is null because its going in the dialog layout
            val layoutView = inflater.inflate(R.layout.update_exercise_layout, null)
            builder.setView(layoutView)

            val exerciseName: EditText = layoutView.findViewById(R.id.et_exerciseName)
            val exerciseDesc: EditText = layoutView.findViewById(R.id.et_exerciseDescription)

            builder.setMessage("My Dialog")
                .setPositiveButton("Well Done!") {dialog, id ->
                    // Perform positive button consequences
                    // TODO: define DialogInterface Listener, it should be able to return EditText.getText()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // Perform negative button consequences
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}