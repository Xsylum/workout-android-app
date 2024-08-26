package com.example.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.WorkoutScheduleAdapter.WorkoutScheduleViewHolder
import com.example.workoutapplication.dataClasses.WorkoutEvent
import java.lang.Integer.min

open class RecyclerViewItem


class WorkoutScheduleAdapter(private val localDataSet: List<WorkoutEvent>,
                             private val listener: WorkoutScheduleListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TYPE_EXISTING = 1
        val TYPE_ADD = 2
    }

    interface WorkoutScheduleListener {
        fun addNewWorkout()
    }

    inner class WorkoutScheduleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val workoutNameTV: TextView
        val workoutExercisesTV: TextView
        val workoutRegimenTV: TextView

        init {
            workoutNameTV = view.findViewById(R.id.tv_workoutScheduleName)
            workoutExercisesTV = view.findViewById(R.id.tv_workoutScheduleExecises)
            workoutRegimenTV = view.findViewById(R.id.tv_workoutScheduleRegimen)
        }
    }

    inner class AddWorkoutViewHolder(view: View): RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val addWorkoutTV: TextView

        init {
            view.setOnClickListener(this)
            addWorkoutTV = view.findViewById(R.id.tv_scheduleAddWorkout)
        }

        override fun onClick(v: View?) {
            listener.addNewWorkout()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == localDataSet.size) {
            TYPE_ADD
        } else {
            TYPE_EXISTING
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
        : RecyclerView.ViewHolder {
        return if (viewType == TYPE_EXISTING) {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.workout_schedule_row_item, viewGroup, false)
            WorkoutScheduleViewHolder(view)
        } else {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.workout_schedule_add_item, viewGroup, false)
            AddWorkoutViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        // change TextView values if the item type is an already scheduled workout
        if (getItemViewType(pos) == TYPE_EXISTING) {
            val holder = viewHolder as WorkoutScheduleViewHolder
            holder.workoutNameTV.text =
                localDataSet[pos].workoutLog.workoutID.toString()

            val workoutRegimen = localDataSet[pos].workoutLog.workoutRegimen
            if (workoutRegimen != null) {
                holder.workoutRegimenTV.text = localDataSet[pos].workoutLog.workoutRegimen!!.name

                // Building preview string of workout's exercises
                val exerciseList = StringBuilder()
                val workoutExerciseList = localDataSet[pos].workoutLog.workoutRegimen!!.exerciseList
                if (workoutExerciseList.size > 0) {
                    val shownExerciseCount = min(workoutExerciseList.size, 5)

                    for (exercise in workoutExerciseList.subList(0, shownExerciseCount - 1)) {
                        exerciseList.append(exercise.name)
                        exerciseList.append(", ")
                    }

                    exerciseList.append(workoutExerciseList[shownExerciseCount - 1])

                    if (workoutExerciseList.size > 5) {
                        exerciseList.append("...")
                    }
                } else {
                    exerciseList.clear()
                    exerciseList.append("No exercises!")
                }

                holder.workoutExercisesTV.text = exerciseList.toString()
            } else {
                holder.workoutRegimenTV.text = "Null Regimen!"
                holder.workoutExercisesTV.text = "No exercises!"
            }
        }
        // No need to change anything for "add workout view"
    }

    override fun getItemCount(): Int {
        // all planned exercises + the "add workout" view
        return localDataSet.size + 1
    }
}