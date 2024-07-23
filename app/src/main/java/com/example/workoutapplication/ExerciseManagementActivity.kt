package com.example.workoutapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class ExerciseManagementActivity : AppCompatActivity(),
    UpdateExerciseFragment.UpdateExerciseDialogListener,
    ExerciseManagementAdapter.ExerciseRecyclerViewListener {

    private var displayList = ArrayList<Exercise>()
    private lateinit var jsonExerciseArray: JSONArray
    private lateinit var recyclerView: RecyclerView

    // Getting DataStore to read/write an ExerciseList Preference
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_management)

        // Retrieving exercise list from DataStore
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList");
        jsonExerciseArray = if (exerciseListJson != null)
            JSONArray(exerciseListJson) else JSONArray()

        // Setting up Activity's list of exercises
        for (i in 0 ..< jsonExerciseArray.length()) {
            val exercise = Exercise.fromJsonString(jsonExerciseArray[i].toString())
            displayList.add(exercise)
        }

        // initializing recyclerView
        recyclerView = findViewById<RecyclerView>(R.id.rv_exerciseManagerList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExerciseManagementAdapter(displayList, this)

        // Modify exercise list buttons
        var addExerciseButton = findViewById<View>(R.id.btn_addExercise) as Button
        addExerciseButton.setOnClickListener {
            showAddExerciseDialog();
        }
    }

    // TODO: prevent fragment from displaying when there is already a fragment on the screen
    override fun onListItemClick(position: Int) {
        val targetExercise = displayList[position]
        showUpdateExerciseDialog(targetExercise.name!!, targetExercise.description!!, position)
    }

    /**
     * Displays dialog fragment for adding a new exercise to the exercise list
     */
    private fun showAddExerciseDialog() {
        val fragment = UpdateExerciseFragment();
        fragment.show(supportFragmentManager, "EXERCISE_ADD_DIALOG")
    }

    private fun showUpdateExerciseDialog(exerciseName:String,
                                         exerciseDesc: String,
                                         listPosition: Int) {
        val fragment = UpdateExerciseFragment(exerciseName, exerciseDesc, listPosition)
        fragment.show(supportFragmentManager, "EXERCISE_UPDATE_DIALOG")
    }

    /**
     * Updates recycler view by notifying of a change in dataSet items
     */
    private fun updateRecyclerViewInsert() {
        recyclerView.adapter!!.notifyItemInserted(displayList.size - 1)
    }

    private fun updateRecyclerViewItemEdit(position: Int) {
        recyclerView.adapter!!.notifyItemChanged(position)
    }

    /**
     * Adds a new exercise to DataStore's ExerciseList json string
     */
    private fun updateDataStoreExerciseList(newExercise: Exercise) {
        val exerciseJsonString = newExercise.toJsonString()

        jsonExerciseArray.put(exerciseJsonString)
        dataStoreHelper.setStringValue("ExerciseList", jsonExerciseArray.toString())
    }

    // User tapped the positive button of the exercise fragment
    override fun onDialogPositiveClick(dialog: DialogFragment, listPosition:Int) {
        val exerciseName: EditText = dialog.requireDialog().findViewById(R.id.et_exerciseName)
        val exerciseDesc: EditText = dialog.requireDialog().findViewById(R.id.et_exerciseDescription)

        val newExercise = Exercise(exerciseName.text.toString(), exerciseDesc.text.toString())
        if (listPosition == -1) {
            displayList.add(newExercise)
            updateRecyclerViewInsert()
        } else {
            displayList[listPosition] = newExercise
            updateRecyclerViewItemEdit(listPosition)
        }

        updateDataStoreExerciseList(newExercise) // TODO: this method have to be splitup to allow faster reordering of exercises in displayList
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    private fun DEBUG_ExerciseList(): JSONArray {
        // TEMP DEBUG EXERCISES
        val tempExercise1 = Exercise("Deadlift", "Lifting bar real cool").apply {
            addTag("legs")
            addTag("Biceps")
            thumbnailID = "001.jpg"
        }
        val tempExercise2 = Exercise("Rows", "Kind of like a boat!").apply {
            addTag("Shoulders")
            thumbnailID = "002.jpg"
        }
        val tempExercise3 = Exercise("Glute Bridge", "Lift dat butt!").apply {
            addTag("Glutes")
            addTag("legs")
            thumbnailID = "003.jpg"
        }
        val tempExercise4 = Exercise("Bicep Curls", "Working that iron").apply {
            addTag("Biceps")
            thumbnailID = "004.jpg"
        }
        val resultArray = JSONArray().apply {
            put(tempExercise1.toJsonString())
            put(tempExercise2.toJsonString())
            put(tempExercise3.toJsonString())
            put(tempExercise4.toJsonString())
        }

        return resultArray
    }
}