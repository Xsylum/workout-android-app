package com.example.workoutapplication;

public class ExerciseStats {

    private Exercise exercise;
    private WorkoutLog partOfWorkout;
    private int measureMetricsMap = 0b0;
    // private ArrayList<Repetition> reps;

    public ExerciseStats(Exercise e, WorkoutLog w)
    {
        exercise = e;
        partOfWorkout = w;
    }

    public int getMeasureMetricsMap()
    {
        return measureMetricsMap;
    }

    public void setMeasureMetricsMap(int bitMap)
    {
        //TODO: check that length is correct! (don't allow 1 where there is no metric definition!)
        measureMetricsMap = bitMap;
    }

    // TODO: implement method, and possibly a facade for toggling metrics by string
    public void toggleMetricInMap(int metric)
    {

    }

}
