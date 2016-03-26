package com.example.alexahern.happy21;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MeditationFragment extends Fragment implements TimerCallback {
    TextView mTextField;
    MeditationTimer timer;
    Button pauseButton;
    Button resetButton;
    Button startButton;
    long lengthOfMeditation = 10000;
    boolean isPaused = false;


    public MeditationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextField = (TextView) view.findViewById(R.id.countdown);
        pauseButton = (Button) view.findViewById(R.id.pausebutton);
        resetButton = (Button) view.findViewById(R.id.resetbutton);
        startButton = (Button) view.findViewById(R.id.startbutton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer == null) {
                    timer = (MeditationTimer) new MeditationTimer(lengthOfMeditation, 100, MeditationFragment.this).start();
                }
                if(isPaused){
                    isPaused = false;
                    timer = (MeditationTimer) new MeditationTimer(timer.getTimeRemaining(), 100, MeditationFragment.this).start();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (timer != null) {
                    timer.cancel();
                }
                isPaused = false;
                timer = (MeditationTimer) new MeditationTimer(lengthOfMeditation, 100, MeditationFragment.this).start();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isPaused && timer != null) {
                    timer.cancel();
                    isPaused = true;
                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void displayTime(String timeRemaining) {
        mTextField.setText(timeRemaining);
    }
}
