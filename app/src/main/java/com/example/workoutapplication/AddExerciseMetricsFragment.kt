package com.example.workoutapplication

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric

class AddExerciseMetricsFragment: Fragment(),
    ExerciseMetricAdditionAdapter.ExerciseMetricAddListener {
    private lateinit var listener: AddMetricsFragListener

    private lateinit var excludedMetrics: ArrayList<ExerciseMetric>
    private lateinit var excludedMetricsRV: RecyclerView

    interface AddMetricsFragListener {
        fun onInsertMetricsClick(frag: AddExerciseMetricsFragment, listPosition: Int)
        fun onCloseAddMetricsClick(frag: AddExerciseMetricsFragment, listPosition: Int)
        fun onCreateMetricClick(frag: AddExerciseMetricsFragment)
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
            throw ClassCastException("$context must implement AddMetricsFragListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_add_metric, container, false)
        val listPos = arguments?.getInt("listPosition") ?: -1

        // close fragment button
        val closeFragTextView = view.findViewById<TextView>(R.id.tv_addableMetricsClose)
        closeFragTextView.setOnClickListener {
            listener.onCloseAddMetricsClick(this, listPos)
        }

        // optional pre-existing metrics list
        excludedMetrics = requireArguments().getSerializable("excludedMetrics")
                as ArrayList<ExerciseMetric>

        excludedMetricsRV = view.findViewById<RecyclerView>(R.id.rv_addableMetrics)
        excludedMetricsRV.layoutManager = LinearLayoutManager(context)
        excludedMetricsRV.adapter = ExerciseMetricAdditionAdapter(excludedMetrics, this)
        excludedMetricsRV.addItemDecoration( // Creates a dividing line between metrics
            DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
        )

        // Creating new metrics functionality
        val addMetricTV = view.findViewById<TextView>(R.id.tv_addNewMetric)
        addMetricTV.setOnClickListener {
            prepareCreateMetric(view)
        }

        val completeMetricTV = view.findViewById<TextView>(R.id.tv_completeNewMetric)
        completeMetricTV.setOnClickListener {
            finishCreateMetric(view)
        }

        return view
    }

    private fun prepareCreateMetric(view: View) {
        val addMetricTV = view.findViewById<TextView>(R.id.tv_addNewMetric)
        addMetricTV.visibility = View.GONE

        val fragmentCreationLayout = view.findViewById<LinearLayout>(R.id.layout_metricDefinition)
        fragmentCreationLayout.visibility = View.VISIBLE
    }

    private fun finishCreateMetric(view: View) {
        listener.onCreateMetricClick(this)

        val fragmentCreationLayout = view.findViewById<LinearLayout>(R.id.layout_metricDefinition)
        fragmentCreationLayout.visibility = View.GONE

        val createMetricTV = view.findViewById<TextView>(R.id.tv_addNewMetric)
        createMetricTV.visibility = View.VISIBLE
    }

    /**
     * Adds a passed metric to the excludedMetrics list, which was built by the
     * activity hosting this fragment upon listener.onCreateMetricClick
     *
     * Helps to avoid constructing metrics twice in memory, and places the responsibility for
     * storing metrics in DataStore on listener
     */
    fun addCreatedMetric(metric: ExerciseMetric) {
        excludedMetrics.add(metric)
        excludedMetricsRV.adapter!!.notifyItemInserted(excludedMetrics.size - 1)
    }

    override fun onMetricClick(position: Int) {
        listener.onInsertMetricsClick(this, position)

        excludedMetrics.removeAt(position)
        excludedMetricsRV.adapter!!.notifyItemRemoved(position)
    }
}