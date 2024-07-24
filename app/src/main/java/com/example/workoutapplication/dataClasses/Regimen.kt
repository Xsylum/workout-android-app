package com.example.workoutapplication.dataClasses

/**
 * A class which allowing the user to define a set of
 * exercises, to act as a plan for the user's workout(s)
 */
class Regimen (var name: String) {
    val exerciseList = ArrayList<Exercise>();

    // Modifying Exercise List Methods
    fun addExercise(e: Exercise) {
        exerciseList.add(e)
    }

    fun removeExercise(e: Exercise) {
        exerciseList.remove(e)
    }

    fun removeExercise(index: Int): Exercise {
        return exerciseList.removeAt(index)
    }

    fun swapExerciseOrder(index1: Int, index2: Int) {
        val temp = exerciseList[index2]
        exerciseList[index2] = exerciseList[index1]
        exerciseList[index1] = temp
    }
}