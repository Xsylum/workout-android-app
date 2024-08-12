package com.example.workoutapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric

// TODO: Split constructor into two that take either an Exercise or nothing
class ExerciseManagementFragment() : Fragment(),
    ExerciseMetricManagementAdapter.ExerciseMetricRemoveViewListener {

    // Interface to deliver action events (calling listener.foo() will call
    // that method in the activity which implements the interface)
    private lateinit var listener: ExerciseManagementFragListener

    var metricList = ArrayList<ExerciseMetric>()
    private lateinit var metricListRV: RecyclerView

    // An activity instantiating this DialogFragment *must*
    // implement this interface, allowing it to receive event callbacks
    //
    // The host can query this dialog using the passed DialogFragment
    interface ExerciseManagementFragListener {
        fun onAddMetricsClick(frag: ExerciseManagementFragment, listPosition: Int)
        fun onExerciseUpdateSave(frag: ExerciseManagementFragment, listPosition: Int)
        fun onExerciseUpdateCancel(frag: ExerciseManagementFragment)
        fun onExerciseDelete(frag: ExerciseManagementFragment, listPosition: Int)
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
            throw ClassCastException("$context must implement ExerciseManagementFragListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_update_info, container, false)
        val listPosition = arguments?.getInt("position", -1) ?: -1

        // Filling EditTexts from Arguments
        try {
            val nameEditText = view.findViewById<EditText>(R.id.et_exerciseName)
            nameEditText.setText(requireArguments().getString("exerciseName"))

            val descEditText = view.findViewById<EditText>(R.id.et_exerciseDescription)
            descEditText.setText(requireArguments().getString("exerciseDesc"))
        } catch (_: IllegalStateException) {
            // Fragment was run without arguments, meaning we are adding a brand new exercise
        } catch (e: NullPointerException) {
            // no value set for a given argument key
            e.printStackTrace()
            throw NullPointerException()
        }

        // Filling metric list from arguments
        try {
            // retrieve and clone the arraylist from arguments, in case update is cancelled
            val exerciseMetricList = requireArguments().getSerializable("exerciseMetrics")
                    as ArrayList<ExerciseMetric>
            metricList = exerciseMetricList.clone() as ArrayList<ExerciseMetric>
        } catch (e: IllegalStateException) {
            // run without adding exerciseList argument, meaning it is a brand new exercise
            metricList = ArrayList()
        } catch (e: NullPointerException) {
            // no value set for a given argument key
            e.printStackTrace()
            throw NullPointerException()
        }

        // Filling Recycler View
        metricListRV = view.findViewById(R.id.rv_addedMetrics)
        metricListRV.layoutManager = LinearLayoutManager(context)
        metricListRV.adapter = ExerciseMetricManagementAdapter(metricList, this)

        // Assigning clickListeners to Buttons
        val addMetricsButton = view.findViewById<Button>(R.id.btn_addExerciseMetric)
        addMetricsButton.setOnClickListener {
            listener.onAddMetricsClick(this, listPosition!!)
        }

        val deleteExerciseTV = view.findViewById<TextView>(R.id.tv_deleteExercise)
        deleteExerciseTV.setOnClickListener {
            listener.onExerciseDelete(this, listPosition!!)
        }

        val cancelUpdateTV = view.findViewById<TextView>(R.id.tv_cancelExerciseUpdate)
        cancelUpdateTV.setOnClickListener {
            listener.onExerciseUpdateCancel(this)
        }

        val saveUpdateTV = view.findViewById<TextView>(R.id.tv_saveExerciseUpdate)
        saveUpdateTV.setOnClickListener {
            listener.onExerciseUpdateSave(this, listPosition!!)
        }

        return view
    }

    fun insertNewMetricToExercise(metric: ExerciseMetric) {
        metricList.add(metric)
        metricListRV.adapter!!.notifyItemInserted(metricList.size - 1)
    }

    override fun onRemoveMetricViewClick(position: Int) {
        TODO("Not yet implemented")
    }
}

