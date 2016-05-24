package com.example.alexahern.happy21;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by alexahern on 26/03/16.
 */
public class MeditationTimer extends CountDownTimer {
    public TimerCallback callback;
    private long timeRemaining;
    int secondsLeft = 0;


    public MeditationTimer(long startTime, long interval, TimerCallback callback){
        super(startTime, interval);
        this.callback = callback;
    }

    public void onTick(long millisUntilFinished) {
        if (Math.round((float)millisUntilFinished / 1000.0f) != secondsLeft)
        {
            secondsLeft = Math.round((float)millisUntilFinished / 1000.0f);
        }
        Log.i("test","ms="+millisUntilFinished+" till finished="+secondsLeft );
        callback.displayTime(String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes(secondsLeft),
            secondsLeft % 60));
        this.timeRemaining = millisUntilFinished;
    }

    @Override
    public void onFinish() {
        callback.displayTime("Done");
    }

    public long getTimeRemaining(){
        return timeRemaining;
    }
}
