package com.example.workoutapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import org.json.JSONArray


// List difference code inspired by https://www.baeldung.com/kotlin/lists-difference
class RegimenDesignActivity : AppCompatActivity(),
    ExerciseManagementAdapter.ExerciseRecyclerViewListener,
    RegimenDesignAddExercisesFragment.AddExercisesListener,
    RegimenRemoveExerciseFragment.RemoveExerciseListener,
    RegimenUpdateFragment.RegimenUpdateListener,
    RegimenDeleteFragment.RegimenDeleteListener {

    // The list of data that is displayed by recyclerView
    private var displayList = ArrayList<Exercise>()
    private lateinit var recyclerView: RecyclerView

    // Info regimen currently being modified
    private lateinit var regimen: Regimen
    private var exercisesNotInRegimen = ArrayList<Exercise>()

    // DataStore variables
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

    private var regimenDataStorePosition: Int = -1 // position where regimen is stored in RegimenList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_design)

        /** Regimen data setup **/
        regimenDataStorePosition = intent
            .getIntExtra("RegimenPosition", regimenDataStorePosition)
        val regimenJson = intent.getStringExtra("TargetRegimen")
        val regimenDataStore = RegimenDataStore.fromJsonString(regimenJson)

        // Get list of exercises to set regimen's exercise list via
        // regimenDataStore's list of exerciseIDs
        val exerciseList = getDataStoreExerciseList();

        // Finally, the regimen being designed can be initialized
        regimen = Regimen(regimenDataStore, exerciseList)

        //TODO add "You haven't added any exercises to this regimen yet" if displayList.size == 0

        // Create a new list of exercises which is = exerciseList - regimen.exerciseList
        exercisesNotInRegimen = findListDifference(exerciseList, regimen.exerciseList)
        Log.d("RegimenTesting", exercisesNotInRegimen.toString())

        // TODO Check that position != -1 [Try to also JUNIT test this]
        /** /Regimen data setup **/

        setUpActivityViews()
    }

    private fun setUpActivityViews() {
        // Setting up the recyclerView to display exercises in this regimen
        recyclerView = findViewById(R.id.rv_regimenExerciseList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Reusing ExerciseManagementAdapter for simplicity
        recyclerView.adapter = ExerciseManagementAdapter(regimen.exerciseList, this)

        val addExerciseButton = findViewById<Button>(R.id.btn_regimenAddExercise)
        addExerciseButton.setOnClickListener {
            showAddExerciseDialog(exercisesNotInRegimen)
        }

        val deleteRegimenButton = findViewById<Button>(R.id.btn_deleteRegimen)
        deleteRegimenButton.setOnClickListener {
            showRegimenDeleteDialog()
        }

        val editRegimenInfoView = findViewById<TextView>(R.id.tv_regimenRename)
        editRegimenInfoView.setOnClickListener {
            showRegimenUpdateDialog(regimen.name!!, regimen.description!!)
        }
    }

    // TODO update this to non-deprecated function
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent()
        intent.putExtra("UPDATED_REGIMEN", regimen.toJsonString())
        intent.putExtra("REGIMEN_POSITION", regimenDataStorePosition)
        setResult(1, intent)
        finish()
    }

    private fun showRegimenDeleteDialog() {
        val fragment = RegimenDeleteFragment(regimen.name!!)
        fragment.show(supportFragmentManager, "DELETE_FRAGMENT_DIALOG")
    }

    /**
     * Return to RegimenManagementActivity with deletion result_code.
     *
     * NOTE: RegimenManagementActivity is expected to deal with the deletion from dataStore!
     */
    override fun onRegimenDeletePositiveClick() {
        val intent = Intent()
        intent.putExtra("DELETED_REGIMEN_POSITION", regimenDataStorePosition)
        setResult(-1, intent)
        finish()
    }

    override fun onListItemClick(adapter:ExerciseManagementAdapter, position: Int) {
        val fragment = RegimenRemoveExerciseFragment(regimen.exerciseList[position].name!!, position)
        fragment.show(supportFragmentManager, "REGIMEN_DELETE_EXERCISE_DIALOG")
    }

    override fun onRemoveExercisePositiveClick(dialog: DialogFragment, position:Int) {
        removeExerciseFromRegimen(position)
    }

    private fun findListDifference(userExercises: ArrayList<Exercise>,
                                   regimenExercises: ArrayList<Exercise>): ArrayList<Exercise> {
        val outputList = (userExercises - regimenExercises.toSet())
        return ArrayList(outputList)
    }

    private fun getDataStoreExerciseList(): ArrayList<Exercise> {
        // Retrieving user metric list from DataStore
        val metricListJson: String? = dataStoreHelper.getStringValue("ExerciseMetricList")
        val jsonMetricArray = if (metricListJson != null) {
            JSONArray(metricListJson)
        } else JSONArray()

        // turn the jsonString metrics into a list of ExerciseMetric objects
        val userMetricArray = ArrayList<ExerciseMetric>()
        for (i in 0..< jsonMetricArray.length()) {
            val metric = ExerciseMetric.fromJsonString(jsonMetricArray[i].toString())
            userMetricArray.add(metric)
        }

        val exerciseListJsonString = dataStoreHelper.getStringValue("ExerciseList")
        val exerciseJsonArray = if (exerciseListJsonString != null) {
            JSONArray(exerciseListJsonString)
        } else JSONArray()

        // Creating LinkedList<Exercise> from DataStore's JSONArray
        val exerciseList = ArrayList<Exercise>()
        for (i in 0 until exerciseJsonArray.length()) {
            val exerciseJsonString = exerciseJsonArray.get(i).toString()
            exerciseList.add(Exercise.fromJsonString(exerciseJsonString, userMetricArray))
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

    /**
     * Sequential method handling the required updates to the regimen's exercise list
     * and calling an update on DataStore's list of regimens
     */
    private fun addNewExercisesToRegimen(exercises: List<Exercise>) {
        val startOfRange = regimen.exerciseList.size

        // update regimen's exercise list and notify the recycler view of additions
        regimen.exerciseList += exercises
        recyclerView.adapter!!.notifyItemRangeInserted(startOfRange, exercises.size)

        exercisesNotInRegimen -= exercises.toSet()

        updateDataStore()
    }

    private fun removeExerciseFromRegimen(position: Int) {
        val removedExercise = regimen.exerciseList.removeAt(position)
        recyclerView.adapter!!.notifyItemRemoved(position)

        exercisesNotInRegimen.add(removedExercise)

        updateDataStore()
    }

    /**
     * Replaces previous version of this regimen held in DataStore with the JsonString
     * of the current version.
     *
     * Because this merely replaces the held data with regimen's current data, this method
     * can be used for updating any changes that solely effect this regimen's values
     */
    private fun updateDataStore() {
        val regimenJsonString = regimen.toJsonString()

        // Get the complete list of regimens, and put the updated JsonString
        // for this regimen in at the proper position
        val listOfRegimens = JSONArray(dataStoreHelper.getStringValue("RegimenList"))
        listOfRegimens.put(regimenDataStorePosition, regimenJsonString)

        // Update the DataStore's regimen list
        dataStoreHelper.setStringValue("RegimenList", listOfRegimens.toString())
    }

    private fun showRegimenUpdateDialog(name: String, description: String) {
        val fragment = RegimenUpdateFragment(name, description)
        fragment.show(supportFragmentManager, "REGIMEN_UPDATE_INFO_DIALOG")
    }

    override fun onRegimenUpdatePositiveClick(name: String, description: String) {
        regimen.name = name
        regimen.description = description

        updateDataStore()
    }

}