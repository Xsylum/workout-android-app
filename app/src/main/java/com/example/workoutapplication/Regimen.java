package com.example.workoutapplication;

import java.util.ArrayList;

/**
 * A class which allowing the user to define a set of
 * exercises, to act as a plan for the user's workout(s)
 */
public class Regimen {

    private String name;
    private ArrayList<Exercise> regimenExercises;

    // Constructor
    public Regimen(String name)
    {
        this.name = name;
    }

    // Variable Getters/Setters
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Exercise> getExerciseList()
    {
        return regimenExercises;
    }

    // Modifying Exercise List Methods

    public void addExercise(Exercise e)
    {
        regimenExercises.add(e);
    }

    public void removeExercise(Exercise e)
    {
        regimenExercises.remove(e);
    }

    public Exercise removeExercise(int index)
    {
        return regimenExercises.remove(index);
    }

    public void swapExerciseOrder(int index1, int index2)
    {
        Exercise temp = regimenExercises.get(index2);
        regimenExercises.set(index2, regimenExercises.get(index1));
        regimenExercises.set(index1, temp);
    }
}
