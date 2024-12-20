package com.example.workertask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button buttonStop;
    static TextView text1;
    static TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        WorkManager.getInstance(this).cancelAllWork(); // Остановка прежних воркеров при перезапуске

        OneTimeWorkRequest workerRequest = new OneTimeWorkRequest.Builder(CounterWorker.class)
                .setInputData(new Data.Builder().putInt("id", 1).build())
                .build();
        OneTimeWorkRequest workerRequest2 = new OneTimeWorkRequest.Builder(CounterWorker.class)
                .setInputData(new Data.Builder().putInt("id", 2).build())
                .build();
        List<OneTimeWorkRequest> list = Arrays.asList(workerRequest, workerRequest2);

        text1 = findViewById(R.id.textView1);
        text2 = findViewById(R.id.textView2);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(v -> {
            WorkManager.getInstance(this).enqueue(list);
        });

        buttonStop.setOnClickListener(v -> {
            WorkManager.getInstance(this).cancelAllWork();
        });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workerRequest.getId()).observe(
                this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d("RRR","Status 1 = "+workInfo.getState());
                    }
                }
        );
    }

    public static void setTextOnView(String t, int id) {
        if (id == 1) text1.setText(t);
        else if (id == 2) text2.setText(t);
    }
}