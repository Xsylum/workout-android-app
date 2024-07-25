package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Regimen

class RegimenManagementActivity : AppCompatActivity(),
    RegimenManagementAdapter.RegimenRecyclerViewListener{

    private var displayList = ArrayList<Regimen>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_management)

        val addRegimenButton: Button = findViewById(R.id.btn_addRegimen)
        addRegimenButton.setOnClickListener {
            addNewRegimen()
        }

        recyclerView = findViewById(R.id.rv_regimenList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RegimenManagementAdapter(displayList, this)
    }

    override fun onListItemClick(position: Int) {
        val targetRegimen = displayList[position]
        Log.d("RegimenTesting", "Regimen = $targetRegimen")
        // TODO have this open RegimenDesignActivity?
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