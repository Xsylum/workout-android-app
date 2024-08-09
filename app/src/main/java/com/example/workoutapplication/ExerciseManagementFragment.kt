package com.example.workoutapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

// TODO: Split constructor into two that take either an Exercise or nothing
class ExerciseManagementFragment() : Fragment(),
    ExerciseMetricManagementAdapter.ExerciseMetricRemoveViewListener {

    // Interface to deliver action events (calling listener.foo() will call
    // that method in the activity which implements the interface)
    private lateinit var listener: ExerciseManagementFragListener

    // An activity instantiating this DialogFragment *must*
    // implement this interface, allowing it to receive event callbacks
    //
    // The host can query this dialog using the passed DialogFragment
    interface ExerciseManagementFragListener {
        fun onAddMetricsClick(fragment: ExerciseManagementFragment, listPosition: Int)
        fun onDialogPositiveClick(fragment: ExerciseManagementFragment, listPosition: Int)
        fun onDialogNeutralClick(fragment: ExerciseManagementFragment)
        fun onDialogNegativeClick(fragment: ExerciseManagementFragment, listPosition: Int)
    }

    /**
     * Called when this fragment is attached to the parameter context (activity)
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Check that the hosting activity implements UpdateExerciseDialogListener
        try {
            // Should be able to cast the activity to the listener interface
            listener = context as ExerciseManagementFragListener
        } catch (e: ClassCastException) {
            // Activity hosting this dialog doesn't implement the interface
            throw ClassCastException("$context must implement UpdateExerciseDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_update_info, container, false)

        val addMetricsButton = view.findViewById<Button>(R.id.btn_addExerciseMetric)
        addMetricsButton.setOnClickListener {
            listener.onAddMetricsClick(this, arguments?.getInt("listPosition") ?: -1)
        }

        try {
            val nameEditText = view.findViewById<EditText>(R.id.et_exerciseName)
            nameEditText.setText(requireArguments().getString("exerciseName"))

            val descEditText = view.findViewById<EditText>(R.id.et_exerciseDescription)
            descEditText.setText(requireArguments().getString("exerciseDesc"))
        } catch (_: IllegalStateException) {
            // Fragment was run without arguments, meaning we are adding a brand new exercise
        }

        return view
    }

    override fun onRemoveMetricViewClick(position: Int) {
        TODO("Not yet implemented")
    }
}

