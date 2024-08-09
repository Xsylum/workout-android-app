package com.example.workoutapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import org.json.JSONArray

class ExerciseManagementActivity : AppCompatActivity(),
    ExerciseManagementFragment.ExerciseManagementDialogListener,
    ExerciseManagementAdapter.ExerciseRecyclerViewListener {

    private var displayList = ArrayList<Exercise>()
    private lateinit var jsonExerciseArray: JSONArray
    private lateinit var recyclerView: RecyclerView

    // Getting DataStore to read/write an ExerciseList Preference
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

    private lateinit var exerciseUpdateFCV: FragmentContainerView
    private lateinit var addableMetricsFCV: FragmentContainerView

    private enum class ActivityStates {
        DEFAULT, UPDATE_FRAGMENT, METRIC_FRAGMENT
    }
    private var currentState = ActivityStates.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_management)

        // Retrieving exercise list from DataStore
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList");
        jsonExerciseArray = if (exerciseListJson != null) {
            JSONArray(exerciseListJson)
        } else JSONArray() // no preference exists yet for exercises

        Log.d("JSONArray", exerciseListJson.toString())

        // Setting up Activity's list of exercises
        for (i in 0 ..< jsonExerciseArray.length()) {
            val exercise = Exercise.fromJsonString(jsonExerciseArray[i].toString())
            Log.d("JSONArray Testing", exercise.toString())
            displayList.add(exercise)
        }

        // initializing recyclerView
        recyclerView = findViewById(R.id.rv_exerciseManagerList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExerciseManagementAdapter(displayList, this)

        exerciseUpdateFCV = findViewById(R.id.fcv_exerciseManageInfo)
        addableMetricsFCV = findViewById(R.id.fcv_exerciseAddableMetrics)

        // Modify exercise list buttons
        var addExerciseButton = findViewById<View>(R.id.btn_addExercise) as Button
        addExerciseButton.setOnClickListener {
            if (currentState == ActivityStates.DEFAULT) {
                showAddExerciseDialog()
            }
        }
    }

    // TODO: prevent fragment from displaying when there is already a fragment on the screen
    override fun onListItemClick(adapter: ExerciseManagementAdapter, position: Int) {
        // display exercise update fragment only if there is no fragment on screen
        if (currentState == ActivityStates.DEFAULT) {
            val targetExercise = displayList[position]
            showUpdateExerciseFragment(targetExercise.name!!, targetExercise.description!!, position)
        }
    }

    /**
     * Displays dialog fragment for adding a new exercise to the exercise list
     */
    private fun showAddExerciseDialog() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ExerciseManagementFragment>(exerciseUpdateFCV.id, "exerciseUpdate")
            addToBackStack("closeExerciseUpdate")
        }
        changeActivityState(ActivityStates.UPDATE_FRAGMENT)
    }

    private fun showUpdateExerciseFragment(exerciseName:String,
                                         exerciseDesc: String,
                                         listPosition: Int) {
        //val fragment = ExerciseManagementFragment(exerciseName, exerciseDesc, listPosition = listPosition)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bundle = Bundle().apply { this.putString("exerciseName", exerciseName);
                this.putString("exerciseDesc", exerciseDesc); this.putInt("position", listPosition)}
            add<ExerciseManagementFragment>(exerciseUpdateFCV.id, "exerciseUpdate", bundle)
            addToBackStack("closeExerciseUpdate")
        }
        changeActivityState(ActivityStates.UPDATE_FRAGMENT)
    }

    override fun onBackPressed() {
        when (currentState) {
            ActivityStates.METRIC_FRAGMENT -> changeActivityState(ActivityStates.UPDATE_FRAGMENT)
            ActivityStates.UPDATE_FRAGMENT -> changeActivityState(ActivityStates.DEFAULT)
            else -> {}
        }

        super.onBackPressed()
    }

    private fun changeActivityState(state: ActivityStates) {
        if (state == ActivityStates.DEFAULT) {
            exerciseUpdateFCV.visibility = View.GONE
            addableMetricsFCV.visibility = View.GONE
        } else if (state == ActivityStates.UPDATE_FRAGMENT) {
            exerciseUpdateFCV.visibility = View.VISIBLE
            addableMetricsFCV.visibility = View.GONE
        } else if (state == ActivityStates.METRIC_FRAGMENT) {
            exerciseUpdateFCV.visibility = View.VISIBLE
            addableMetricsFCV.visibility = View.VISIBLE
        }
        currentState = state
    }

    /**
     * Updates recyclerView that an exercise has been inserted at the end of the display list
     */
    private fun updateRecyclerViewInsert() {
        recyclerView.adapter!!.notifyItemInserted(displayList.size - 1)
    }

    private fun updateRecyclerViewItemEdit(position: Int) {
        recyclerView.adapter!!.notifyItemChanged(position)
    }

    private fun updateRecyclerViewDelete(position: Int) {
        recyclerView.adapter!!.notifyItemRemoved(position)
    }

    /**
     * Adds a new exercise to DataStore's ExerciseList json string
     */
    private fun dataStoreExerciseInsert(newExercise: Exercise) {
        val exerciseJsonString = newExercise.toJsonString()

        jsonExerciseArray.put(exerciseJsonString)
        dataStoreHelper.setStringValue("ExerciseList", jsonExerciseArray.toString())
    }

    private fun dataStoreExerciseUpdate(e: Exercise, position:Int) {
        val exerciseJsonString = e.toJsonString()

        jsonExerciseArray.put(position, exerciseJsonString)
        dataStoreHelper.setStringValue("ExerciseList", jsonExerciseArray.toString())
    }

    private fun dataStoreExerciseDelete(position: Int) {
        jsonExerciseArray.remove(position)
        dataStoreHelper.setStringValue("ExerciseList", jsonExerciseArray.toString())
    }

    /**
     * Adds a new exercise to ExerciseList based on fragment information
     *
     * Executed when ExerciseManagementDialogListener broadcasts that the
     * fragment's positive button was clicked
     */
    override fun onDialogPositiveClick(dialog: ExerciseManagementFragment, position:Int) {
        /**
        val exerciseName: EditText = dialog.requireDialog().findViewById(R.id.et_exerciseName)
        val exerciseDesc: EditText = dialog.requireDialog()
            .findViewById(R.id.et_exerciseDescription)
        val metricList: ArrayList<ExerciseMetric> = dialog.exerciseMetricList

        if (position == -1) { // Adding a new exercise
            val newExercise = Exercise(exerciseName.text.toString(), exerciseDesc.text.toString())
            newExercise.trackingMetrics = metricList
            displayList.add(newExercise)

            updateRecyclerViewInsert()
            dataStoreExerciseInsert(newExercise)
        } else { // updating an existing exercise
            val targetExercise = displayList[position]
            targetExercise.name = exerciseName.text.toString()
            targetExercise.description = exerciseDesc.text.toString()
            targetExercise.trackingMetrics = metricList

            updateRecyclerViewItemEdit(position)
            dataStoreExerciseUpdate(targetExercise, position)
        }
        */
    }

    override fun onDialogNeutralClick(dialog: ExerciseManagementFragment) { /** No Effect **/ }

    /**
     * Deletes an exercise from ExerciseList after using the update fragment
     *
     * Executed when ExerciseManagementDialogListener broadcasts that the
     * fragment's negative button was clicked.
     * Note that the negative button only appears when attempting to edit an existing
     * exercise, never when adding a new one.
     */
    override fun onDialogNegativeClick(dialog: ExerciseManagementFragment, position: Int) {
        val targetExercise = displayList[position]

        displayList.removeAt(position)
        updateRecyclerViewDelete(position)

        dataStoreExerciseDelete(position)
    }
}