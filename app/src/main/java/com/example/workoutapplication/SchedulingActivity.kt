package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class SchedulingActivity : AppCompatActivity() {

    lateinit var calendar: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduling)

        calendar = findViewById(R.id.workoutCalendar)

        val jumpTodayButton = findViewById<Button>(R.id.btn_jumpToToday)
        jumpTodayButton.setOnClickListener {
            val date = Calendar.getInstance()
            val currentTimeInMillis = date.timeInMillis

            calendar.date = currentTimeInMillis
        }
    }
}