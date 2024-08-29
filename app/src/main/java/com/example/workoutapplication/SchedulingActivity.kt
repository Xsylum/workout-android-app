package com.example.workoutapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapplication.dataClasses.Regimen
import com.example.workoutapplication.dataClasses.WorkoutEvent
import com.example.workoutapplication.dataClasses.WorkoutLog
import com.example.workoutapplication.databinding.CalendarDayLayoutBinding
import com.example.workoutapplication.WorkoutScheduleAdapter.WorkoutScheduleListener
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.YearMonth

class SchedulingActivity : AppCompatActivity(), WorkoutScheduleListener {

    lateinit var calendarView: com.kizitonwose.calendar.view.CalendarView
    val events = mutableMapOf<LocalDate, MutableList<WorkoutEvent>>() /** First time using kotlin collections */
    var selectedDay: LocalDate = LocalDate.now()

    lateinit var scheduleRecyclerView: RecyclerView

    private lateinit var activityResultLaunch: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduling)

        val existingEvents = getDataStoreObjectList(DataStoreKey.WORKOUT_EVENT,
            getDataStoreHelper(this)) as ArrayList<WorkoutEvent>
        for (event in existingEvents) {
            if (events[event.date] != null) {
                events[event.date]!!.add(event)
            } else {
                events[event.date] = mutableListOf(event)
            }
        }



        activityResultLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result ->
            when (result.resultCode) {
                // New WorkoutLog created
                0 -> {
                    // get the new workout
                    val newWorkoutPos = result.data?.getIntExtra(
                        "WORKOUT_POSITION", -1)
                    val workoutList = (getDataStoreObjectList(DataStoreKey.WORKOUT,
                        getDataStoreHelper(this)) as ArrayList<WorkoutLog>)
                    val workout = workoutList[newWorkoutPos!!]
                    val newEvent = WorkoutEvent(workout, selectedDay)

                    // add workout to selected day schedule
                    if (events[selectedDay] != null) {
                        events[selectedDay]!!.add(newEvent)
                    } else { // also need to initialize the schedule list
                        events[selectedDay] = mutableListOf(newEvent)
                    }

                    // save new event to DataStore
                    setDataStoreAtListPosition(DataStoreKey.WORKOUT_EVENT, -1,
                        newEvent, getDataStoreHelper(this))

                    // update display list
                    displayWorkoutScheduleForDay(selectedDay)
                }
            }
        }

        calendarView = findViewById(R.id.workoutCalendar)


        val jumpTodayButton = findViewById<Button>(R.id.btn_jumpToToday)
        jumpTodayButton.setOnClickListener {

        }

        // Container providing access to the views for any day's layout on the calendar
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // set when container is bound
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    selectedDay = day.date
                    Log.d("DaySelection", "$selectedDay")
                    displayWorkoutScheduleForDay(selectedDay)
                }
            }
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
                        if (events[data.date].orEmpty().isNotEmpty()) {
                            eventDotView.makeVisible()
                            val workoutLog = events[data.date]!!.first()

                            eventDotView.background.changeColor(Color.parseColor(
                                workoutLog.dotColour.hexColour))
                        } else {
                            eventDotView.makeInvisible()
                        }
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

        scheduleRecyclerView = findViewById(R.id.rv_iteneraryWorkouts)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        displayWorkoutScheduleForDay(selectedDay)
    }

    fun displayWorkoutScheduleForDay(date: LocalDate) {
        val dateTV = findViewById<TextView>(R.id.tv_iteneraryDate)
        dateTV.text = date.toString()

        val newScheduleAdapter =
            WorkoutScheduleAdapter(events[date] ?: ArrayList(), this)
        scheduleRecyclerView.swapAdapter(newScheduleAdapter, true)

        // update the event dot for this date
        calendarView.notifyDateChanged(date)
    }

    override fun addNewWorkout() {
        val intent = Intent(this, WorkoutLogActivity::class.java)
        intent.putExtra("WorkoutDSPosition", -1)

        activityResultLaunch.launch(intent)
    }
}