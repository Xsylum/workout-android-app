package com.example.workoutapplication;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

/**
 * Concept and core-code structure sourced from the following Medium article
 * https://medium.com/@deadmanapple/using-the-android-datastore-library-instead-of-sharedpreferences-in-java-d6744c348a05
 */
public class DataStoreSingleton {
    RxDataStore<Preferences> dataStore;

    // The singleton that all activities will call when using DataStore
    private static final DataStoreSingleton singletonInstance = new DataStoreSingleton();
    
    public static DataStoreSingleton getInstance(Context context)
    {
        if (singletonInstance.dataStore == null) {
            singletonInstance.dataStore = new RxPreferenceDataStoreBuilder(context, "MyData").build();
        }

        return singletonInstance;
    }

    private DataStoreSingleton() { }

    public void setDataStore(RxDataStore<Preferences> dataStore)
    {
        this.dataStore = dataStore;
    }

    public RxDataStore<Preferences> getDataStore()
    {
        return dataStore;
    }

}
