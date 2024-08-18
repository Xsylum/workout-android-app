package com.example.workoutapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.ExerciseMetric
import org.json.JSONArray

class ExerciseManagementActivity : AppCompatActivity(),
    ExerciseManagementFragment.ExerciseManagementFragListener,
    ExerciseManagementAdapter.ExerciseRecyclerViewListener,
    AddExerciseMetricsFragment.AddMetricsFragListener {

    private lateinit var jsonMetricArray: JSONArray
    private var userMetricArray = ArrayList<ExerciseMetric>()

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

        // Retrieving user metric list from DataStore
        val metricListJson: String? = dataStoreHelper.getStringValue("ExerciseMetricList")
        jsonMetricArray = if (metricListJson != null) {
            JSONArray(metricListJson)
        } else JSONArray()

        // turn the jsonString metrics into a list of ExerciseMetric objects
        userMetricArray = ArrayList()
        for (i in 0..< jsonMetricArray.length()) {
            val metric = ExerciseMetric.fromJsonString(jsonMetricArray[i].toString())
            userMetricArray.add(metric)
        }

        // Retrieving exercise list from DataStore
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList");
        jsonExerciseArray = if (exerciseListJson != null) {
            JSONArray(exerciseListJson)
        } else JSONArray() // no preference exists yet for exercises

        // Setting up Activity's list of exercises
        for (i in 0 ..< jsonExerciseArray.length()) {
            val exercise = Exercise.fromJsonString(jsonExerciseArray[i].toString(), userMetricArray)
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
            showUpdateExerciseFragment(targetExercise, position)
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

    private fun showUpdateExerciseFragment(e: Exercise, listPosition: Int) {
        //val fragment = ExerciseManagementFragment(exerciseName, exerciseDesc, listPosition = listPosition)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bundle = Bundle().apply { this.putString("exerciseName", e.name);
                this.putString("exerciseDesc", e.description); this.putInt("position", listPosition)
                this.putSerializable("exerciseMetrics", e.trackingMetrics)}
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
            if (supportFragmentManager.findFragmentByTag("exerciseUpdate") != null) {
                supportFragmentManager.popBackStack("closeExerciseUpdate",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            exerciseUpdateFCV.makeGone()
            addableMetricsFCV.makeGone()
        } else if (state == ActivityStates.UPDATE_FRAGMENT) {
            if (supportFragmentManager.findFragmentByTag("metricUpdate") != null) {
                supportFragmentManager.popBackStack("closeMetricUpdate",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            exerciseUpdateFCV.makeVisible()
            addableMetricsFCV.makeGone()
        } else if (state == ActivityStates.METRIC_FRAGMENT) {
            exerciseUpdateFCV.makeVisible()
            addableMetricsFCV.makeVisible()
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

    private fun dataStoreCreateMetric(m: ExerciseMetric) {
        userMetricArray.add(m)
        val jsonMetric = m.toJsonString()

        jsonMetricArray.put(jsonMetric)
        dataStoreHelper.setStringValue("ExerciseMetricList", jsonMetricArray.toString())
    }

    private fun showAddMetricsFragment() {

        // get the list of exercise metrics not included in this current exercise
        val updateFrag = supportFragmentManager.findFragmentByTag("exerciseUpdate")
            as ExerciseManagementFragment
        val excludedMetrics = ArrayList(userMetricArray - updateFrag.metricList.toSet())

        // open the metric management fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            // pass the metrics not-included in the exercise
            val bundle = Bundle().apply { this.putSerializable("excludedMetrics", excludedMetrics)}
            add<AddExerciseMetricsFragment>(addableMetricsFCV.id,
                "metricUpdate", bundle)
            addToBackStack("closeMetricUpdate")
        }

        changeActivityState(ActivityStates.METRIC_FRAGMENT)
    }

    /**
     * Open the metric management fragment due to the user wanting to add new
     * tracking metrics to a given exercise (from that exercise's management fragment)
     */
    override fun onAddMetricsClick(frag: ExerciseManagementFragment, listPosition: Int) {
        if (currentState == ActivityStates.UPDATE_FRAGMENT) {
            showAddMetricsFragment()
        }
    }

    override fun onInsertMetricsClick(frag: AddExerciseMetricsFragment, metric: ExerciseMetric) {
        val updateFrag = supportFragmentManager.findFragmentByTag("exerciseUpdate")
                as ExerciseManagementFragment
        updateFrag.insertNewMetricToExercise(metric)
    }

    override fun onCloseAddMetricsClick(frag: AddExerciseMetricsFragment) {
        supportFragmentManager.popBackStack("closeMetricUpdate", 0)
        changeActivityState(ActivityStates.UPDATE_FRAGMENT)
    }

    /**
     * Create a new (not-assigned) ExerciseMetric when AddExerciseMetricsFragment sends
     * a signal that the new metric's data is available
     */
    override fun onCreateMetricClick(frag: AddExerciseMetricsFragment) {
        // new metric data
        val newMetricName = frag.requireActivity()
            .findViewById<EditText>(R.id.et_newMetricName).text.toString()
        val newMetricUnit = frag.requireActivity()
            .findViewById<EditText>(R.id.et_newMetricUnit).text.toString()
        val isTimeStat = frag.requireActivity()
            .findViewById<CheckBox>(R.id.check_newMetricTime).isChecked

        // create the new metric
        val outputMetric = ExerciseMetric(newMetricName, if (isTimeStat) 1 else 0, newMetricUnit)

        frag.addCreatedMetric(outputMetric)
        dataStoreCreateMetric(outputMetric)
    }

    /**
     * Adds a new exercise to ExerciseList based on fragment information
     *
     * Executed when ExerciseManagementDialogListener broadcasts that the
     * fragment's positive button was clicked
     */
    override fun onExerciseUpdateSave(frag: ExerciseManagementFragment, listPosition:Int) {
        val exerciseName: EditText = frag.requireActivity().findViewById(R.id.et_exerciseName)
        val exerciseDesc: EditText = frag.requireActivity()
        .findViewById(R.id.et_exerciseDescription)
        val metricList: ArrayList<ExerciseMetric> = frag.metricList

        if (listPosition == -1) { // Adding a new exercise
        val newExercise = Exercise(exerciseName.text.toString(), exerciseDesc.text.toString())
        newExercise.trackingMetrics = metricList
        displayList.add(newExercise)

        updateRecyclerViewInsert()
        dataStoreExerciseInsert(newExercise)
        } else { // updating an existing exercise
        val targetExercise = displayList[listPosition]
        targetExercise.name = exerciseName.text.toString()
        targetExercise.description = exerciseDesc.text.toString()
        targetExercise.trackingMetrics = metricList

        updateRecyclerViewItemEdit(listPosition)
        dataStoreExerciseUpdate(targetExercise, listPosition)
        }

        changeActivityState(ActivityStates.DEFAULT)
    }

    override fun onExerciseUpdateCancel(dialog: ExerciseManagementFragment) {
        changeActivityState(ActivityStates.DEFAULT)
    }

    /**
     * Deletes an exercise from ExerciseList after using the update fragment
     *
     * Executed when ExerciseManagementDialogListener broadcasts that the
     * fragment's negative button was clicked.
     * Note that the negative button only appears when attempting to edit an existing
     * exercise, never when adding a new one.
     */
    override fun onExerciseDelete(dialog: ExerciseManagementFragment, position: Int) {
        if (position == -1) {
            //TODO stop button from appearing for -1?
        } else {
            displayList.removeAt(position)
            updateRecyclerViewDelete(position)

            dataStoreExerciseDelete(position)
        }

        changeActivityState(ActivityStates.DEFAULT)
    }
}