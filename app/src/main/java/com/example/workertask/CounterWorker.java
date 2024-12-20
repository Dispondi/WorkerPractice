package com.example.workertask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CounterWorker extends Worker {

    int idText;

    public CounterWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);
        idText = workerParameters.getInputData().getInt("id", -1);
    }

    @NonNull
    @Override
    public Result doWork() {
        int counter = 0;
        Handler handler = new Handler(Looper.getMainLooper());

        while(!isStopped()) {
            try {
                Thread.sleep(1000);
                if (counter == 30) counter = 0; else counter++;
                int finalCounter = counter;
                handler.post(() -> {
                    MainActivity.setTextOnView(Integer.toString(finalCounter), idText);
                });
            } catch (InterruptedException e) {
                return Result.failure();
            }
        }
        return Result.success();
    }
}

