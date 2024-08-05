package com.example.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.ExerciseStatAdapter.ExerciseStatViewHolder
import com.example.workoutapplication.ExerciseSetAdapter.ExerciseSetViewHolder
import com.example.workoutapplication.MetricValueAdapter.MetricValueViewHolder
import com.example.workoutapplication.dataClasses.ExerciseMetricValue
import com.example.workoutapplication.dataClasses.ExerciseStats

/**
 * Adapter for handling the vertical layout of exercise stats in WorkoutLogActivity
 */
class ExerciseStatAdapter(private val localDataSet: ArrayList<ExerciseStats>)
    : RecyclerView.Adapter<ExerciseStatViewHolder>() {

    inner class ExerciseStatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val exerciseNameTextView: TextView
        val exerciseSetsRecyclerView: RecyclerView

        init {
            exerciseNameTextView = view.findViewById(R.id.tv_exerciseStatName)
            exerciseSetsRecyclerView = view.findViewById(R.id.rv_exerciseSets)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ExerciseStatViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.exercise_stat_row_item, viewGroup, false)
        return ExerciseStatViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ExerciseStatViewHolder, position: Int) {
        viewHolder.exerciseNameTextView.text = localDataSet[position].exercise.name
        viewHolder.exerciseSetsRecyclerView.layoutManager = LinearLayoutManager(
            viewHolder.exerciseSetsRecyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        viewHolder.exerciseSetsRecyclerView.adapter =
            ExerciseSetAdapter(localDataSet[position].metricDataGrid)
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}

/**
 * Adapter for handling the horizontal layout of an ExerciseStat's sets
 */
internal class ExerciseSetAdapter(private val localDataSet: ArrayList<ArrayList<ExerciseMetricValue>>) :
    RecyclerView.Adapter<ExerciseSetViewHolder>() {

    inner class ExerciseSetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val setNumberTextView: TextView
        val metricValueRecyclerView: RecyclerView

        init {
            setNumberTextView = view.findViewById(R.id.tv_setNumberTitle)
            metricValueRecyclerView = view.findViewById(R.id.rv_metricValues)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ExerciseSetViewHolder {
        // Create a new view defining the UI of the list view
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.exercise_set_column_item, viewGroup, false)
        return ExerciseSetViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ExerciseSetViewHolder, position: Int) {
        viewHolder.setNumberTextView.text = "Set ${position + 1}"
        viewHolder.metricValueRecyclerView.layoutManager =
            LinearLayoutManager(viewHolder.metricValueRecyclerView.context)
        viewHolder.metricValueRecyclerView.adapter = MetricValueAdapter(localDataSet[position])
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}

/**
 * Adapter for handling the vertical layout of MetricValues
 * within each of ExerciseStat's sets
 */
internal class MetricValueAdapter(private val localDataSet: ArrayList<ExerciseMetricValue>)
    : RecyclerView.Adapter<MetricValueViewHolder>() {

    inner class MetricValueViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val metricValueEditText: EditText

        init {
            metricValueEditText = view.findViewById(R.id.et_metricValueInput)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MetricValueViewHolder {
        // Create a new view defining the UI of the list view
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.exercise_metric_value_row_item, viewGroup, false)
        return MetricValueViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MetricValueViewHolder, position: Int) {
        //TODO get metric's "Unit of measurement"
        viewHolder.metricValueEditText.setText(localDataSet[position].valStringFormat)
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}