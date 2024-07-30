package com.example.workoutapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import org.json.JSONArray
import java.util.LinkedList


// List difference code inspired by https://www.baeldung.com/kotlin/lists-difference
class RegimenDesignActivity : AppCompatActivity(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener,
    RegimenDesignAddExercisesFragment.AddExercisesListener {

    // The list of data that is displayed by recyclerView
    private var displayList = ArrayList<Exercise>()
    private lateinit var recyclerView: RecyclerView

    // Info regimen currently being modified
    private lateinit var regimen: Regimen
    private var dataStorePosition: Int = -1 // position where regimen is stored in RegimenList
    private var exercisesNotInRegimen = ArrayList<Exercise>()

    // DataStore variables
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_design)

        /** Regimen data setup **/
        dataStorePosition = intent.getIntExtra("RegimenPosition", dataStorePosition)
        val regimenJson = intent.getStringExtra("TargetRegimen")
        val regimenDataStore = RegimenDataStore.fromJsonString(regimenJson)

        // Get list of exercises to set regimen's exercise list via
        // regimenDataStore's list of exerciseIDs
        val exerciseList = getDataStoreExerciseList();

        // Finally, the regimen being designed can be initialized
        regimen = Regimen(regimenDataStore, exerciseList)
        displayList = regimen.exerciseList

        //TODO add "You haven't added any exercises to this regimen yet" if displayList.size == 0

        // Create a new list of exercises which is = exerciseList - regimen.exerciseList
        exercisesNotInRegimen = findListDifference(exerciseList, regimen.exerciseList)
        Log.d("RegimenTesting", exercisesNotInRegimen.toString())

        // TODO Check that position != -1 [Try to also JUNIT test this]
        /** /Regimen data setup **/

        // Setting up the recyclerView to display exercises in this regimen
        recyclerView = findViewById(R.id.rv_regimenExerciseList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Reusing ExerciseManagementAdapter for simplicity
        recyclerView.adapter = ExerciseManagementAdapter(displayList, this)

        recyclerView = findViewById(R.id.rv_regimenExerciseList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Reusing ExerciseManagementAdapter for simplicity
        recyclerView.adapter = ExerciseManagementAdapter(displayList, this)

        val addExerciseButton = findViewById<Button>(R.id.btn_regimenAddExercise)
        addExerciseButton.setOnClickListener() {
            showAddExerciseDialog(exercisesNotInRegimen)
        }
    }

    override fun onListItemClick(adapter:ExerciseManagementAdapter, position: Int) {
        TODO("Not yet implemented")
    }

    private fun findListDifference(userExercises: ArrayList<Exercise>,
                                   regimenExercises: ArrayList<Exercise>): ArrayList<Exercise> {
        val outputList = (userExercises - regimenExercises.toSet())
        return ArrayList(outputList)
    }

    private fun getDataStoreExerciseList(): ArrayList<Exercise> {
        val exerciseListJsonString = dataStoreHelper.getStringValue("ExerciseList")
        val exerciseJsonArray = if (exerciseListJsonString != null) {
            JSONArray(exerciseListJsonString)
        } else JSONArray()

        // Creating LinkedList<Exercise> from DataStore's JSONArray
        val exerciseList = ArrayList<Exercise>()
        for (i in 0 until exerciseJsonArray.length()) {
            val exerciseJsonString = exerciseJsonArray.get(i).toString()
            exerciseList.add(Exercise.fromJsonString(exerciseJsonString))
        }

        return exerciseList
    }

    private fun showAddExerciseDialog(userExercises: ArrayList<Exercise>) {
        // TODO maybe change this to a Bottom Sheet (pull up screen)
        val fragment = RegimenDesignAddExercisesFragment(userExercises)
        fragment.show(supportFragmentManager, "REGIMEN_ADD_EXERCISE_DIALOG")
    }

    /**
     * After positive button click in fragment from showAddExerciseDialog(), grab the
     * addedExercises list and call an update on this regimen's data holders
     */
    override fun onExerciseAddPositiveClick(dialog: DialogFragment) {
        val addExercisesFragment = dialog as RegimenDesignAddExercisesFragment
        val addedExercises = addExercisesFragment.addedExercises

        addNewExercisesToRegimen(addedExercises)
    }

    private fun addNewExercisesToRegimen(exercises: List<Exercise>) {
        val startOfRange = displayList.size

        displayList += exercises
        recyclerView.adapter!!.notifyItemRangeInserted(startOfRange, exercises.size)

        exercisesNotInRegimen -= exercises.toSet()
    }

}