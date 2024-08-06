package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.ExerciseMetric

// TODO: Split constructor into two that take either an Exercise or nothing
class ExerciseManagementFragment(private val exerciseName: String = "",
                                 private val exerciseDesc: String = "",
                                 val exerciseMetricList:
                                    ArrayList<ExerciseMetric> = ArrayList(),
                                 private val listPosition: Int = -1) : DialogFragment(),
    ExerciseMetricManagementAdapter.ExerciseMetricRemoveViewListener {

    // Interface to deliver action events (calling listener.foo() will call
    // that method in the activity which implements the interface)
    private lateinit var listener: ExerciseManagementDialogListener

    // An activity instantiating this DialogFragment *must*
    // implement this interface, allowing it to receive event callbacks
    //
    // The host can query this dialog using the passed DialogFragment
    interface ExerciseManagementDialogListener {
        fun onDialogPositiveClick(dialog: ExerciseManagementFragment, listPosition: Int)
        fun onDialogNeutralClick(dialog: ExerciseManagementFragment)
        fun onDialogNegativeClick(dialog: ExerciseManagementFragment, listPosition: Int)
    }

    /**
     * Called when this fragment is attached to the parameter context (activity)
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Check that the hosting activity implements UpdateExerciseDialogListener
        try {
            // Should be able to cast the activity to the listener interface
            listener = context as ExerciseManagementDialogListener
        } catch (e: ClassCastException) {
            // Activity hosting this dialog doesn't implement the interface
            throw ClassCastException("$context must implement UpdateExerciseDialogListener")
        }
    }

    /**
     * Defines the dialog container to be built using update_exercise_layout.xml
     */
    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            // Inflating/setting the layout for the dialog
            // Parent view is null because its going in the dialog layout
            val layoutView = inflater.inflate(R.layout.fragment_exercise_update_info, null)
            builder.setView(layoutView)

            // Setting the default EditText text contents, either empty for "Add Exercise" or
            // the existing data when updating an exercise (from the Fragment's constructor
            val nameEditText: EditText = layoutView.findViewById(R.id.et_exerciseName)
            nameEditText.setText(exerciseName)
            val descriptionEditText: EditText = layoutView.findViewById(R.id.et_exerciseDescription)
            descriptionEditText.setText(exerciseDesc)

            val metricRecyclerView: RecyclerView = layoutView.findViewById(R.id.rv_exerciseMetrics)
            metricRecyclerView.layoutManager = LinearLayoutManager(context)
            metricRecyclerView.adapter =
                ExerciseMetricManagementAdapter(exerciseMetricList, this) // TODO LEFT OFF HERE

            // Positive and Neutral buttons exist when adding or updating an exercise
            builder.setPositiveButton("Confirm") {dialog, id ->
                    // Perform positive button consequences
                    listener.onDialogPositiveClick(this, listPosition)
                }
                .setNeutralButton("Cancel") { dialog, id ->
                    listener.onDialogNeutralClick(this)

                }

            // Only when updating an existing exercise should the user be able to
            // delete the exercise from the ExerciseList
            if (listPosition != -1) {
                builder.setMessage("Update Exercise")
                    .setNegativeButton("Delete") { dialog, id ->
                        // Inform the activity to perform the "negative action" method
                        listener.onDialogNegativeClick(this, listPosition)
                    }
            } else {
                builder.setMessage("Add New Exercise")
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onRemoveMetricViewClick(position: Int) {
        TODO("Not yet implemented")
    }
}

