package com.example.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.ExerciseManagementAdapter.ExerciseViewHolder
import com.example.workoutapplication.ExerciseMetricManagementAdapter.ExerciseMetricViewHolder
import com.example.workoutapplication.ExerciseMetricAdditionAdapter.ExerciseIncludeMetricViewHolder
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric

// TODO force any activity using this adapter to ensure it implements interface ExerciseRecyclerViewListener
class ExerciseManagementAdapter (private val localDataSet: ArrayList<Exercise>,
                                 private val listener: ExerciseRecyclerViewListener)
    : RecyclerView.Adapter<ExerciseViewHolder>() {

    interface ExerciseRecyclerViewListener {
        fun onListItemClick(adapter:ExerciseManagementAdapter, position: Int)
    }

    // Providing a reference to type of views being used
    // (custom ViewHolder)
    inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val exerciseThumbnailView: ImageView
        val exerciseNameView: TextView
        val exerciseDescription: TextView

        // ViewHolder constructed using a given view (which it will hold)
        init {
            // When this view (list item) is clicked, this class will be returned
            // as an interface View.OnClickListener, which can then run onClick()
            view.setOnClickListener(this)

            // in the view, find the element with id textView
            exerciseThumbnailView = view.findViewById(R.id.iv_exercise_thumbnail)
            exerciseNameView = view.findViewById(R.id.tv_exercise_name)
            exerciseDescription = view.findViewById(R.id.tv_exercise_description)
        }

        /**
         * Alerts the listener this view was clicked, and provides it with the view's position
         * and the parent adapter (allowing listener to have several lists
         * which are using this adapter class)
         */
        override fun onClick(v: View?) {
            val position = adapterPosition
            // NO_POSITION is returned if the user clicks a
            // removed item while the animation is running
            if (position != RecyclerView.NO_POSITION)
                listener.onListItemClick(this@ExerciseManagementAdapter, position)
        }
    }

    // Create new views (will be invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ExerciseViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.exercise_row_item, viewGroup, false)
        return ExerciseViewHolder(view)
    }

    // Replace the contents of a view (will be invoked by layout manager)
    // Alternatively: Adds/changes data in the ViewHolder (different views!)
    override fun onBindViewHolder(viewHolder: ExerciseViewHolder, position: Int) {

        // Get element from your dataset at this position and
        // replace the contents of the view with that element
        viewHolder.exerciseNameView.text = localDataSet[position].name
        viewHolder.exerciseDescription.text = localDataSet[position].description
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}

/**
 * For displaying a management version of an exercise's metrics, which includes a textView/button
 * which can remove the metric from the exercise on touch.
 */
class ExerciseMetricManagementAdapter(private val localDataSet: ArrayList<ExerciseMetric>,
                                      private val listener: ExerciseMetricRemoveViewListener)
    : RecyclerView.Adapter<ExerciseMetricViewHolder>() {
    interface ExerciseMetricRemoveViewListener {
        fun onRemoveMetricViewClick(position: Int)
    }

    // Providing a reference to type of views being used
    // (custom ViewHolder)
    inner class ExerciseMetricViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val metricName: TextView
        val removeMetricView: TextView

        // ViewHolder constructed using a given view (which it will hold)
        init {
            metricName = view.findViewById(R.id.tv_manageMetricName)
            removeMetricView = view.findViewById(R.id.tv_removeMetric)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
    : ExerciseMetricViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.exercise_manage_metrics_row_item, viewGroup, false)
        return ExerciseMetricViewHolder(view)
    }

    // Adds/changes data in the ViewHolder (different views!)
    override fun onBindViewHolder(viewHolder: ExerciseMetricViewHolder, position: Int) {

        // Get element from your dataset at this position and
        // replace the contents of the view with that element
        viewHolder.metricName.text = localDataSet[position].metricName

        /**
         * Remove this metric from this exercise
         */
        viewHolder.removeMetricView.setOnClickListener {
            val metricPosition = viewHolder.adapterPosition

            if (metricPosition != RecyclerView.NO_POSITION) {
                listener.onRemoveMetricViewClick(metricPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}

class ExerciseMetricAdditionAdapter(private val localDataSet: ArrayList<ExerciseMetric>,
                                    private val listener: ExerciseMetricAddListener)
    : RecyclerView.Adapter<ExerciseIncludeMetricViewHolder>() {
    interface ExerciseMetricAddListener {
        fun onMetricClick(position: Int)
    }

    // Providing a reference to type of views being used
    // (custom ViewHolder)
    inner class ExerciseIncludeMetricViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val metricName: TextView
        val metricFormat: TextView

        // ViewHolder constructed using a given view (which it will hold)
        init {
            view.setOnClickListener(this)

            metricName = view.findViewById(R.id.tv_metricName)
            metricFormat = view.findViewById(R.id.tv_metricValueFormat)
        }

        override fun onClick(v: View?) {
            listener.onMetricClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
            : ExerciseIncludeMetricViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.exercise_metric_row_item, viewGroup, false)
        return ExerciseIncludeMetricViewHolder(view)
    }

    // Adds/changes data in the ViewHolder (different views!)
    override fun onBindViewHolder(viewHolder: ExerciseIncludeMetricViewHolder, position: Int) {

        // Get element from your dataset at this position and
        // replace the contents of the view with that element
        viewHolder.metricName.text = localDataSet[position].metricName
        viewHolder.metricFormat.text = localDataSet[position].getFormatTypeAsString()
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}