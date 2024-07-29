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

        // Modify exercise list buttons
        var addExerciseButton = findViewById<View>(R.id.btn_addExercise) as Button
        addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }
    }

    // TODO: prevent fragment from displaying when there is already a fragment on the screen
    override fun onListItemClick(adapter: ExerciseManagementAdapter, position: Int) {
        val targetExercise = displayList[position]
        showUpdateExerciseDialog(targetExercise.name!!, targetExercise.description!!, position)
    }

    /**
     * Displays dialog fragment for adding a new exercise to the exercise list
     */
    private fun showAddExerciseDialog() {
        val fragment = ExerciseManagementFragment();
        fragment.show(supportFragmentManager, "EXERCISE_ADD_DIALOG")
    }

    private fun showUpdateExerciseDialog(exerciseName:String,
                                         exerciseDesc: String,
                                         listPosition: Int) {
        val fragment = ExerciseManagementFragment(exerciseName, exerciseDesc, listPosition)
        fragment.show(supportFragmentManager, "EXERCISE_UPDATE_DIALOG")
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
    override fun onDialogPositiveClick(dialog: DialogFragment, position:Int) {
        val exerciseName: EditText = dialog.requireDialog().findViewById(R.id.et_exerciseName)
        val exerciseDesc: EditText = dialog.requireDialog()
            .findViewById(R.id.et_exerciseDescription)

        // TODO change how dataStoreExerciseInsert works to allow updating an existing jsonString
        // Creating a new object even if just updating to simplify dataStore insertion
        if (position == -1) {
            val newExercise = Exercise(exerciseName.text.toString(), exerciseDesc.text.toString())
            displayList.add(newExercise)
            updateRecyclerViewInsert()

            dataStoreExerciseInsert(newExercise)
        } else {
            val targetExercise = displayList[position]
            targetExercise.name = exerciseName.text.toString()
            targetExercise.description = exerciseDesc.text.toString()
            updateRecyclerViewItemEdit(position)

            dataStoreExerciseUpdate(targetExercise, position)
        }
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) { /** No Effect **/ }

    /**
     * Deletes an exercise from ExerciseList after using the update fragment
     *
     * Executed when ExerciseManagementDialogListener broadcasts that the
     * fragment's negative button was clicked.
     * Note that the negative button only appears when attempting to edit an existing
     * exercise, never when adding a new one.
     */
    override fun onDialogNegativeClick(dialog: DialogFragment, position: Int) {
        val targetExercise = displayList[position]

        displayList.removeAt(position)
        updateRecyclerViewDelete(position)

        dataStoreExerciseDelete(position)
    }
}