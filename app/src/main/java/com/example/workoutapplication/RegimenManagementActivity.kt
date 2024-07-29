package com.example.workoutapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Exercise
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.RegimenDataStore
import org.json.JSONArray
import java.util.LinkedList

class RegimenManagementActivity : AppCompatActivity(),
    RegimenManagementAdapter.RegimenRecyclerViewListener{

    private var displayList = ArrayList<Regimen>()
    private lateinit var jsonRegimenArray: JSONArray
    private lateinit var recyclerView: RecyclerView

    // DataStore to read/write RegimenList Preference
    private val dataStoreSingleton = DataStoreSingleton.getInstance(this)
    private val dataStoreHelper = DataStoreHelper(this, dataStoreSingleton.dataStore!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_management)

        val myRegimen = Regimen("hello", "goodbye")

        val addRegimenButton: Button = findViewById(R.id.btn_addRegimen)
        addRegimenButton.setOnClickListener {
            addNewRegimen()
        }

        // Getting the json-list of exercises from DataStore
        val exerciseListJson: String? = dataStoreHelper.getStringValue("ExerciseList")
        val jsonExerciseArray = if (exerciseListJson != null) {
            JSONArray(exerciseListJson)
        } else JSONArray()

        // Creating List of exercises from json for
        // Regimen(RegimenDataStore, LinkedList<Exercises>)
        val exerciseList = LinkedList<Exercise>()
        for (i in 0 until jsonExerciseArray.length()) {
            val exerciseJsonString = jsonExerciseArray.get(i).toString()
            exerciseList.add(Exercise.fromJsonString(exerciseJsonString))
        }

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

            displayList.add(regimen)
        }

        Log.d("Testing", displayList.toString())

        // Setting up the recyclerView of regimens
        recyclerView = findViewById(R.id.rv_regimenList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RegimenManagementAdapter(displayList, this)
    }

    override fun onListItemClick(position: Int) {
        val targetRegimen = displayList[position]
        Log.d("RegimenTesting", "Regimen = $targetRegimen")

        val intent = Intent(this, RegimenDesignActivity::class.java)
        intent.putExtra("TargetRegimen", targetRegimen.toJsonString())
        intent.putExtra("RegimenPosition", position)

        startActivity(intent)
    }

    private fun addNewRegimen(name: String = "TestRegimen", description: String = "TestDescription") {
        val outputRegimen = Regimen(name, description)
        displayList.add(outputRegimen)

        updateRecyclerViewInsert()
        dataStoreRegimenInsert(outputRegimen, displayList.size - 1)
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