package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.ExerciseMetric

// TODO: Split constructor into two that take either an Exercise or nothing
class ExerciseManagementFragment(private val exerciseName: String = "",
                                 private val exerciseDesc: String = "",
                                 val exerciseMetricList:
                                    ArrayList<ExerciseMetric> = ArrayList(),
                                 private val listPosition: Int = -1) : Fragment(),
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_update_info, container, false)

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

