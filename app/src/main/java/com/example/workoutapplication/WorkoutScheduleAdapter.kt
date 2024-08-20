package com.example.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.workoutapplication.WorkoutScheduleAdapter.WorkoutScheduleViewHolder
import com.example.workoutapplication.dataClasses.WorkoutEvent
import org.w3c.dom.Text

class WorkoutScheduleAdapter(private val localDataSet: List<WorkoutEvent>)
    : RecyclerView.Adapter<WorkoutScheduleViewHolder>() {

    inner class WorkoutScheduleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val workoutNameTV: TextView
        val workoutExercisesTV: TextView
        val workoutRegimenTV: TextView

        init {
            workoutNameTV = view.findViewById(R.id.tv_WorkoutScheduleName)
            workoutExercisesTV = view.findViewById(R.id.tv_WorkoutScheduleExecises)
            workoutRegimenTV = view.findViewById(R.id.tv_WorkoutScheduleRegimen)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
        : WorkoutScheduleViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.workout_schedule_row_item, viewGroup, false)
        return WorkoutScheduleViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: WorkoutScheduleViewHolder, pos: Int) {
        viewHolder.workoutNameTV.text = localDataSet[pos].workoutLog.workoutID.toString()

        val workoutRegimen = localDataSet[pos].workoutLog.workoutRegimen
        if (workoutRegimen != null) {
            viewHolder.workoutRegimenTV.text = localDataSet[pos].workoutLog.workoutRegimen!!.name

            // Building preview string of workout's exercises
            val exerciseList = StringBuilder()
            val workoutExerciseList = localDataSet[pos].workoutLog.workoutRegimen!!.exerciseList
            if (workoutExerciseList.size != 0) {
                for (exercise in workoutExerciseList.subList(0, 5)) {
                    exerciseList.append(exercise.name)
                    exerciseList.append(", ")
                }

                exerciseList.append(workoutExerciseList[5])

                if (workoutExerciseList.size > 5) {
                    exerciseList.append("...")
                }
            } else {
                exerciseList.clear()
                exerciseList.append("No exercises!")
            }

            viewHolder.workoutExercisesTV.text = exerciseList.toString()
        } else {
            viewHolder.workoutRegimenTV.text = "Null Regimen!"
            viewHolder.workoutExercisesTV.text = "No exercises!"
        }

    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}