package com.example.workoutapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import org.json.JSONArray
import java.util.LinkedList

class RegimenManagementActivity : AppCompatActivity(),
    RegimenManagementAdapter.RegimenRecyclerViewListener,
    RegimenUpdateFragment.RegimenUpdateListener {

    private var displayList = ArrayList<Regimen>()
    private var exerciseList = ArrayList<Exercise>()
    private lateinit var jsonRegimenArray: JSONArray
    private lateinit var recyclerView: RecyclerView

    private lateinit var activityResultLaunch: ActivityResultLauncher<Intent>

    // DataStore to read/write RegimenList Preference
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_management)

        exerciseList = getUserExercises()
        displayList = getUserRegimens()

        // Setup the launcher which will process any future result from RegimenDesignActivity
        activityResultLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result ->
            when (result.resultCode) {
                // REGIMEN WAS DELETED
                -1 -> {
                    val deletedRegimenPosition = result.data
                        ?.getIntExtra("DELETED_REGIMEN_POSITION", -1)!!

                    deleteRegimen(deletedRegimenPosition)
                }
                // No Changes to the regimen
                0 -> {}
                // Regimen was altered
                1 -> {
                    val rDS = RegimenDataStore.fromJsonString(
                        result.data?.getStringExtra("UPDATED_REGIMEN"))
                    val updatedRegimen = Regimen(rDS, exerciseList)
                    val regimenPosition = result.data?.getIntExtra("REGIMEN_POSITION", -1)!!

                    displayList[regimenPosition] = updatedRegimen
                    updateRecyclerViewRegimenUpdated(regimenPosition)
                }
                else -> {}
            }
        }

        setUpActivityViews()
    }

    private fun setUpActivityViews() {
        // Setting up the recyclerView of regimens
        recyclerView = findViewById(R.id.rv_regimenList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RegimenManagementAdapter(displayList, this)

        val addRegimenButton: Button = findViewById(R.id.btn_addRegimen)
        addRegimenButton.setOnClickListener {
            addNewRegimen()
        }
    }

    private fun getUserExercises(): ArrayList<Exercise> {
        val outputList = ArrayList<Exercise>()

        // Getting the json-list of exercises from DataStore
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList")
        val jsonExerciseArray = if (exerciseListJson != null) {
            JSONArray(exerciseListJson)
        } else JSONArray()

        // Creating List of exercises from json for
        // Regimen(RegimenDataStore, LinkedList<Exercises>)
        for (i in 0 until jsonExerciseArray.length()) {
            val exerciseJsonString = jsonExerciseArray.get(i).toString()
            outputList.add(Exercise.fromJsonString(exerciseJsonString))
        }

        return outputList
    }

    private fun getUserRegimens(): ArrayList<Regimen> {
        val outputList = ArrayList<Regimen>()

        // Getting the list of regimens from DataStore
        val regimenListJson: String? = dataStoreHelper.getStringValue("RegimenList")
        jsonRegimenArray = if (regimenListJson != null) {
            JSONArray(regimenListJson)
        } else JSONArray() // no preference exists yet for regimens

        // Creating the regimens based on the json list
        // and adding them to the display list
        for (i in 0 until jsonRegimenArray.length()) {
            val regimenJsonString = jsonRegimenArray.get(i).toString()
            val dataStoreRegimen = RegimenDataStore.fromJsonString(regimenJsonString)
            val regimen = Regimen(dataStoreRegimen, exerciseList)

            outputList.add(regimen)
        }

        return outputList
    }

    override fun onListItemClick(position: Int) {
        val targetRegimen = displayList[position]
        Log.d("RegimenTesting", "Regimen = $targetRegimen")

        // Create the intent for launching RegimenDesignActivity
        val intent = Intent(this, RegimenDesignActivity::class.java)
        intent.putExtra("TargetRegimen", targetRegimen.toJsonString())
        intent.putExtra("RegimenPosition", position)

        activityResultLaunch.launch(intent)
    }

    private fun addNewRegimen(name: String = "TestRegimen", description: String = "TestDescription") {
        val fragment = RegimenUpdateFragment()
        fragment.show(supportFragmentManager, "ADD_REGIMEN_DIALOG")
    }

    override fun onRegimenUpdatePositiveClick(name: String, description: String) {
        val newRegimen = Regimen(name, description)
        displayList.add(newRegimen)

        updateRecyclerViewInsert()
        dataStoreRegimenInsert(newRegimen, displayList.size - 1)
    }

    private fun deleteRegimen(position: Int): Regimen {
        val targetRegimen = displayList.removeAt(position)

        updateRecyclerViewDelete(position)
        dataStoreRegimenDelete(position)

        return targetRegimen
    }

    private fun updateRecyclerViewInsert() {
        recyclerView.adapter!!.notifyItemInserted(displayList.size - 1)
    }

    private fun updateRecyclerViewDelete(position: Int) {
        recyclerView.adapter!!.notifyItemRemoved(position)
    }

    private fun updateRecyclerViewRegimenUpdated(position: Int) {
        recyclerView.adapter!!.notifyItemChanged(position)
    }

    /** DATA STORE METHODS **/
    private fun dataStoreRegimenInsert(r: Regimen, position: Int) {
        val regimenJsonString = r.toJsonString()

        jsonRegimenArray.put(regimenJsonString)
        dataStoreHelper.setStringValue("RegimenList", jsonRegimenArray.toString())
    }

    private fun dataStoreRegimenDelete(position: Int) {
        jsonRegimenArray.remove(position)
        dataStoreHelper.setStringValue("RegimenList", jsonRegimenArray.toString())
    }
}