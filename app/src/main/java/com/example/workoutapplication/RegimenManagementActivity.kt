package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Regimen

class RegimenManagementActivity : AppCompatActivity() {

    private var displayList = ArrayList<Regimen>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regimen_management)

        recyclerView = findViewById(R.id.rv_regimenList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RegimenManagementAdapter(displayList, this)
    }
}