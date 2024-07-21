package com.example.workoutapplication

import android.app.Activity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Concept and core-code structure sourced from the following Medium article
 * https://medium.com/@deadmanapple/using-the-android-datastore-library-instead-of-sharedpreferences-in-java-d6744c348a05
 *
 * This class implements methods to store Key-data in a DataStore in a fashion
 * similar to SharedPreferences. Activities making use of this Helper should source
 * their reference to DataStore from the DataStoreSingleton, as required in the class' documentation
 */
class DataStoreHelper(var activity: Activity, private var dataStore: DataStore<Preferences>) {

    // NOTE, to put any other type of value (bool, int, etc.) another
    // version of the below "putXValue" and "getXValue" will need to be created
    fun setStringValue(key: String, value: String): Boolean {
        // Get the Preferences.Key associated with the input key
        val prefKey = stringPreferencesKey(key);

        // Try to insert value at the key, but return
        val updateResult = try {
            runBlocking {
                dataStore.edit { prefsIn ->
                    prefsIn[prefKey] = value
                }
            }
        } catch (e: Exception){
            return false
        }

        return true
    }

    fun getStringValue(key: String): String {
        val prefKey = stringPreferencesKey(key)
        val value = runBlocking {
            val preferences = dataStore.data.first()
            preferences[prefKey];
        }
        return value ?: "null"
    }
}