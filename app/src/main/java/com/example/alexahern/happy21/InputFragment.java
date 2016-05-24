package com.example.alexahern.happy21;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by alexahern on 26/03/16.
 */


public class InputFragment extends Fragment {
    TextView mInputText;
    TextView mInstructionsText;
    TextView mLoadText;
    TextView mDateLoadedText;

    Button saveButton;
    Button loadButton;
    String dateSelected;
    static String EXTRA_MESSAGE= "text";
    static String EXTRA_MESSAGE_TWO = "type";
    String message;
    String type;

public static InputFragment newInstance(String name, String type){
    InputFragment f = new InputFragment();
    Bundle bdl = new Bundle(1);
    bdl.putString(EXTRA_MESSAGE, name);
    bdl.putString(EXTRA_MESSAGE_TWO, type);
    f.setArguments(bdl);
    return f;
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = getArguments().getString(EXTRA_MESSAGE);
        type = getArguments().getString(EXTRA_MESSAGE_TWO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        Bundle args = getArguments();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInputText = (TextView) view.findViewById(R.id.input_edit);
        mInstructionsText = (TextView) view.findViewById(R.id.instructions_text) ;
        saveButton = (Button) view.findViewById(R.id.save_button);
        loadButton = (Button) view.findViewById(R.id.load_button);
        mLoadText = (TextView) view.findViewById(R.id.load_text) ;
        mDateLoadedText = (TextView) view.findViewById(R.id.date_loaded);

        mInstructionsText.setText(message);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInput();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dateSelected = ""+dayOfMonth+"-"+(monthOfYear+1);
            loadInput(dateSelected);
        }
    };


        private void showDatePicker() {
            DatePickerFragment date = new DatePickerFragment();
            date.setCallback(ondate);
            date.show(getFragmentManager(), "Date Picker");
    }

    private void loadInput(String date){
        String FILENAME = type+"-" + date;
        try {
            FileInputStream fis = getContext().openFileInput(FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
            String entryForDay = "";
            String line = "";
            while ((line = reader.readLine()) != null){
                entryForDay += line;
            }
            mDateLoadedText.setText(date);
            mLoadText.setText(entryForDay);
        } catch (FileNotFoundException e) {
            Toast.makeText(getContext(), "File not found", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveInput() {
        Boolean fileFound=false;
        Calendar calendar = Calendar.getInstance();
        String test = "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1);
        final String FILENAME = type+"-" + test;
        for (String s : getContext().fileList()) {
            if (s.equals(FILENAME)) {
                fileFound=true;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to overwrite today?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
                        FileOutputStream fos = null;
                        try {
                            fos = getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(mInputText.getText().toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        if (!fileFound){
            FileOutputStream fos = null;
            try {
                fos = getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(mInputText.getText().toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

        public void setCallback(DatePickerDialog.OnDateSetListener ondate){
            listener = ondate;
        }


    }


}
