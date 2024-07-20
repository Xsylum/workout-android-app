package com.example.workoutapplication;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;

/**
 * Concept and core-code structure sourced from the following Medium article
 * https://medium.com/@deadmanapple/using-the-android-datastore-library-instead-of-sharedpreferences-in-java-d6744c348a05
 *
 * This class implements methods to store Key-data in a DataStore in a fashion
 * similar to SharedPreferences. Activities making use of this Helper should source
 * their reference to DataStore from the DataStoreSingleton, as required in the class' documentation
 */
public class DataStoreHelper {
    Activity activity;
    RxDataStore<Preferences> dataStoreRx;
    Preferences pref_error = new Preferences() {
        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }

        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return new HashMap<>();
        }
    }; //TODO: autofilled this, check if the non-abstract code causes errors

    public DataStoreHelper(Activity activity, RxDataStore<Preferences> dataStoreRx)
    {
        this.activity = activity;
        this.dataStoreRx = dataStoreRx;
    }

    // NOTE, to put any other type of value (bool, int, etc.) another
    // version of the below "putXValue" and "getXValue" will need to be created
    public boolean setStringValue(String key, String value)
    {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(key);
        Single<Preferences> updateResult = dataStoreRx.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);

        return updateResult.blockingGet() != pref_error;
    }

    String getStringValue(String Key)
    {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<String> value = dataStoreRx.data().firstOrError()
                .map(prefs -> prefs.get(PREF_KEY));//.onErrorReturnItem("getStringValueNull");
        return value.blockingGet();
    }



}
