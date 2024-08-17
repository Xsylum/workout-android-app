package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import java.time.YearMonth

class SchedulingActivity : AppCompatActivity() {

    lateinit var calendarView: com.kizitonwose.calendar.view.CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduling)

        calendarView = findViewById(R.id.workoutCalendar)


        val jumpTodayButton = findViewById<Button>(R.id.btn_jumpToToday)
        jumpTodayButton.setOnClickListener {

        }

        // providing binder for calendar
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }

        // setting up desired dates
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(160)
        val endMonth = currentMonth.plusMonths(200)
        val firstDayOfWeek = firstDayOfWeekFromLocale()

        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

    }
}