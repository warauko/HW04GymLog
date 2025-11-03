package com.example.hw04_gymlog_v300;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw04_gymlog_v300.database.GymLogRepository;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    com.example.hw04_gymlog_v300.databinding.ActivityMainBinding binding;

    private GymLogRepository repository;
    public static final String TAG = "DAC_GYMLOG";

    String mExercise = "";
    double mWeight = 0.0;
    int mReps = 0;

    int loggedInUserID = -1;
    //TODO: ADD LOGIN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());
        repository = GymLogRepository.getRepository(getApplication());
        updateDisplay();

    binding.logButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getInformationFromDisplay();
            insertGymLogRecord();
            updateDisplay();

        }
    });

    binding.exerciseInputEditText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           updateDisplay();
        }
    });

    }

    private void insertGymLogRecord(){
        if (mExercise.isEmpty()) {
            return;
        }

        GymLog log = new GymLog(mExercise, mWeight, mReps, loggedInUserID);
        repository.insertGymLog(log);
    }

    private void updateDisplay() {
        if (repository == null) {
            return;
        }
        ArrayList<GymLog> allLogs = repository.getAllLogs();
        StringBuilder sb = new StringBuilder();
        if (allLogs.isEmpty()) {
            binding.logDisplayTextView.setText(R.string.nothing_to_show_time_to_hit_the_gym);
        }
        for(GymLog log : allLogs) {
            sb.append(log);
        }

        binding.logDisplayTextView.setText(sb.toString());
    }

    private void getInformationFromDisplay(){
        mExercise = binding.exerciseInputEditText.getText().toString();
        try {
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from Weight edit text.");
        }

        try {
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from Reps edit text.");
        }


    }
}