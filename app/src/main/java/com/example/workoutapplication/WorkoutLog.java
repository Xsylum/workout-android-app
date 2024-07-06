package com.example.workoutapplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.Date;

public class WorkoutLog {

    private LocalDate date;
    private LocalTime timeOfCompletion;
    private Regimen workoutRegimen;
    private ArrayList<ExerciseStats[]> exerciseStats;

    public WorkoutLog(LocalDate date)
    {
        this.date = date; //TODO: check if better to have non-date constructor
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(int year, int month, int dayOfMonth)
    {
        this.date = LocalDate.of(year, month, dayOfMonth);
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}
