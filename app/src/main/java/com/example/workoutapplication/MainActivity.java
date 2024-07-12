package com.example.workoutapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button nextPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxDataStore<Preferences> dataStoreRx;

        // SEE DataStoreSingleton.java for credits and more information
        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
        if (dataStoreSingleton.getDataStore() == null)
        {
            dataStoreRx = new RxPreferenceDataStoreBuilder(this, "AppData").build();
        }
        else
        {
            dataStoreRx = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRx);


        nextPageButton = (Button) findViewById(R.id.nextPageButton);

        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ExerciseManagementActivity.class));
            }
        });
    }
}