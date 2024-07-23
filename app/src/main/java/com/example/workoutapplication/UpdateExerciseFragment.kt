package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class UpdateExerciseFragment : DialogFragment() {

    // Interface to deliver action events (calling listener.foo() will call
    // that method in the activity which implements the interface)
    internal lateinit var listener: UpdateExerciseDialogListener

    // An activity instantiating this DialogFragment *must*
    // implement this interface, allowing it to receive event callbacks
    //
    // The host can query this dialog using the passed DialogFragment
    interface UpdateExerciseDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Called when this fragment is attached to a given context (activity)
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Check that the hosting activity implements UpdateExerciseDialogListener
        try {
            // Should be able to cast the activity to the listener interface
            listener = context as UpdateExerciseDialogListener
        } catch (e: ClassCastException) {
            // Activity hosting this dialog doesn't implement the interface
            throw ClassCastException("$context must implement UpdateExerciseDialogListener")
        }
    }

    // Defining a custom Dialog container
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;

            // Inflating/setting the layout for the dialog
            // Parent view is null because its going in the dialog layout
            val layoutView = inflater.inflate(R.layout.update_exercise_layout, null)
            builder.setView(layoutView)

            builder.setMessage("My Dialog")
                .setPositiveButton("Well Done!") {dialog, id ->
                    // Perform positive button consequences
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // Inform the activity to perform the "negative action" method
                    listener.onDialogNegativeClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}