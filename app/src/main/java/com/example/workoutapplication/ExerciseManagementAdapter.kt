package com.example.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.ExerciseManagementAdapter.ExerciseViewHolder

class ExerciseManagementAdapter (private val localDataSet: ArrayList<Exercise>) : RecyclerView.Adapter<ExerciseViewHolder>() {

    // Providing a reference to type of views being used
    // (custom ViewHolder)
    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exerciseThumbnailView: ImageView
        val exerciseNameView: TextView
        val exerciseDescription: TextView

        // ViewHolder constructed using a given view (which it will hold)
        init {

            // NOTE: could define click listener here

            // in the view, find the element with id textView
            exerciseThumbnailView = view.findViewById<View>(R.id.iv_exercise_thumbnail) as ImageView
            exerciseNameView = view.findViewById<View>(R.id.tv_exercise_name) as TextView
            exerciseDescription = view.findViewById<View>(R.id.tv_exercise_description) as TextView
        }
    }

    // Create new views (will be invoked by the layout manager
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