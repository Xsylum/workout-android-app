package com.example.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.RegimenManagementAdapter.RegimenViewHolder

class RegimenManagementAdapter(private val localDataSet: ArrayList<Regimen>,
                               private val listener: RegimenRecyclerViewListener)
    : RecyclerView.Adapter<RegimenViewHolder>() {

    interface RegimenRecyclerViewListener {
        fun onListItemClick(position: Int)
    }

    inner class RegimenViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
            val regimenNameView: TextView
            val regimenDescriptionView: TextView
            val regimenExampleExercisesView: TextView

            // ViewHolder is constructed used the type of view it will hold
            init {
                view.setOnClickListener(this)

                regimenNameView = view.findViewById(R.id.tv_regimen_name)
                regimenDescriptionView = view.findViewById(R.id.tv_regimen_description)
                regimenExampleExercisesView = view.findViewById(R.id.tv_regimen_exercise_examples)
            }

            override fun onClick(v: View?) {
                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION)
                    listener.onListItemClick(position)
            }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RegimenViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.regimen_row_item, viewGroup, false)
        return RegimenViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RegimenViewHolder, position: Int) {

        // Get element from your dataset at this position and
        // replace the contents of the view with that element
        viewHolder.regimenNameView.text = localDataSet[position].name
        viewHolder.regimenDescriptionView.text = localDataSet[position].description
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}