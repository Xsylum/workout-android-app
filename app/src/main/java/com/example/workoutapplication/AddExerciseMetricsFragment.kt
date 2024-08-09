package com.example.workoutapplication

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.ExerciseMetric

class AddExerciseMetricsFragment: Fragment() {
    private lateinit var listener: AddMetricsFragListener

    interface AddMetricsFragListener {
        fun onInsertMetricsClick(frag: AddExerciseMetricsFragment, listPosition: Int)
        fun onCloseAddMetricsClick(frag: AddExerciseMetricsFragment, listPosition: Int)
    }

    /**
     * Called when this fragment is attached to the parameter context (activity)
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Check that the hosting activity implements UpdateExerciseDialogListener
        try {
            // Should be able to cast the activity to the listener interface
            listener = context as AddMetricsFragListener
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
        val view = inflater.inflate(R.layout.fragment_exercise_add_metric, container, false)

        val listPos = arguments?.getInt("listPosition") ?: -1

        val closeFragTextView = view.findViewById<Button>(R.id.tv_addableMetricsClose)
        closeFragTextView.setOnClickListener {
            listener.onCloseAddMetricsClick(this, listPos)
        }

        val excludedMetricsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable("excludedMetrics", ArrayList::class.java)
        } else {
            requireArguments().getSerializable("excludedMetrics") as ArrayList<ExerciseMetric>
        }

        val metricsRV = view.findViewById<RecyclerView>(R.id.rv_addableMetrics)
        metricsRV.layoutManager = LinearLayoutManager(context)
        metricsRV.adapter = null //TODO

        return view
    }
}