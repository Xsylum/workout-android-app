package com.example.workoutapplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile

private val Context.dataStore by preferencesDataStore("MyData")

/**
 * Concept and core-code structure sourced from the following Medium article
 * https://medium.com/@deadmanapple/using-the-android-datastore-library-instead-of-sharedpreferences-in-java-d6744c348a05
 */
class DataStoreSingleton private constructor() {

    var dataStore: DataStore<Preferences>? = null

    companion object {
        // The singleton that all activities will call when using DataStore
        private var singletonInstance: DataStoreSingleton? = null

        fun getInstance(context: Context): DataStoreSingleton {
            var result = singletonInstance ?: DataStoreSingleton().also { singletonInstance = it }

            if (result.dataStore == null) {
                result.dataStore = context.dataStore
            }

            return result
        }
    }

}