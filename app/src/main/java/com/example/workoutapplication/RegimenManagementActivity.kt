package com.example.workoutapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Regimen
import org.json.JSONArray

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

        // Getting the list of regimens from DataStore
        val regimenListJson: String? = dataStoreHelper.getStringValue("RegimenList")
        jsonRegimenArray = if (regimenListJson != null) {
            JSONArray(regimenListJson)
        } else JSONArray() // no preference exists yet for regimens

        // Setting up the recyclerView of regimens
        recyclerView = findViewById(R.id.rv_regimenList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RegimenManagementAdapter(displayList, this)
    }

    override fun onListItemClick(position: Int) {
        val targetRegimen = displayList[position]
        Log.d("RegimenTesting", "Regimen = $targetRegimen")

        startActivity(
            Intent(
                this,
                RegimenDesignActivity::class.java
            )
        )
    }

    private fun addNewRegimen(name: String = "TestRegimen", description: String = "TestDescription") {
        val outputRegimen = Regimen(name, description)

        Log.d("RegimenTest", displayList.add(outputRegimen).toString())
        updateRecyclerViewInsert()
    }

    private fun updateRecyclerViewInsert() {
        recyclerView.adapter!!.notifyItemInserted(displayList.size - 1)
    }
}