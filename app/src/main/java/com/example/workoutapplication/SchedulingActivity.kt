package com.example.workoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.workoutapplication.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate
import java.time.YearMonth

class SchedulingActivity : AppCompatActivity() {

    data class Event(val id: String, val text: String, val date: LocalDate)

    lateinit var calendarView: com.kizitonwose.calendar.view.CalendarView
    val events = mutableMapOf<LocalDate, List<Event>>()

    fun DEBUG_CreateEvents() {
        val test = LocalDate.of(2024, 8, 12)
        val testEvent = Event("TestEvent", "This is a Test", test)
        val eventList = mutableListOf(testEvent)

        events.put(test, eventList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduling)

        //TODO remove this testing method
        DEBUG_CreateEvents()

        calendarView = findViewById(R.id.workoutCalendar)


        val jumpTodayButton = findViewById<Button>(R.id.btn_jumpToToday)
        jumpTodayButton.setOnClickListener {

        }

        // Container providing access to the views for any day's layout on the calendar
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // set when container is bound
            val binding = CalendarDayLayoutBinding.bind(view)
        }

        // providing binder for calendar
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.calendarDayText
                val eventDotView = container.binding.eventDotView

                textView.makeVisible()
                eventDotView.makeGone()

                textView.text = data.date.dayOfMonth.toString()

                when (data.date) {
                    LocalDate.now() -> {
                        eventDotView.makeVisible()
                    }
                    else -> {
                        eventDotView.isVisible = events[data.date].orEmpty().isNotEmpty()
                    }
                }
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