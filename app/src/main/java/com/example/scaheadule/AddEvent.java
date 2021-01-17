package com.example.scaheadule;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scaheadule.Schedule.Scheduler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEvent extends Fragment {
    View view;
    EditText tName;
    EditText tTime;
    Spinner tCategory;
    CalendarView tCalendar;
    EditText tDescription;

    Button clearButton;
    Button saveButton;

    Scheduler sch;

    //Old fragment version of CreateEvent
    public AddEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_event, container, false);

        initialiseUI();
        return view;
    }

    private void initialiseUI()
    {
        tName = view.findViewById(R.id.editName);
        tTime = view.findViewById(R.id.editTime);
        tCalendar = view.findViewById(R.id.calendarAdd);
        tCategory = view.findViewById(R.id.spinnerCategory);
        tDescription = view.findViewById(R.id.editDescription);

        MainActivity ma = (MainActivity)getActivity();
        sch = ma.loadScheduler();

        List<String> categories = new ArrayList<>();
        categories.add("General");
        categories.add("Assignments");
        categories.add("Holidays");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        tCategory.setAdapter(arrayAdapter);

        saveButton = view.findViewById(R.id.btnSave);
        clearButton = view.findViewById(R.id.btnClear);

        tCalendar.setOnDateChangeListener(calendarhandler);
        saveButton.setOnClickListener(saveHandler);
        clearButton.setOnClickListener(clearHandler);

        Calendar calendar = Calendar.getInstance();
        selectedYear = String.valueOf(calendar.get(Calendar.YEAR));
        selectedMonth = String.valueOf(calendar.get(Calendar.MONTH));
        selecteddayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    }

    private Boolean tryParse()
    {
        Boolean result = false;
        if (tName.getText().toString().isEmpty() || tTime.getText().toString().isEmpty() || tDescription.getText().toString().isEmpty())
        {
            Toast.makeText(view.getContext(),"Please make sure all fields are filled.", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Date time1 = new SimpleDateFormat("HH:mm").parse(tTime.getText().toString());
                result = true;

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "Make sure that all fields are filled out correctly.", Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }

    private String GenerateLine()
    {
        String result = "";
        String resultDate = selecteddayOfMonth + "-" + selectedMonth + "-" + selectedYear;
        result = resultDate + "<>" + tTime.getText().toString() + "<>" + tName.getText().toString() + "<>" + tCategory.getSelectedItem().toString() + "<>"
                + tDescription.getText().toString();
        return result;
    }

    private void SaveData(String sBody)
    {
        try {
            String sFileName = "schedule_storage.txt";
            File root = new File(sFileName);
            if (!root.exists()) {
                root.mkdirs();
            }

            OutputStreamWriter writer = new OutputStreamWriter(view.getContext().openFileOutput(sFileName, Context.MODE_PRIVATE));
            writer.append(sBody);
            writer.append(GenerateLine());
            writer.flush();
            writer.close();
            Toast.makeText(view.getContext(), "Saved @" + root.getCanonicalPath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String CreateRaw()
    {
        StringBuilder text = new StringBuilder();
        for (String s : sch.getRawData())
        {
            text.append(s);
            text.append('\n');
        }
        return text.toString();
    }

    private String selectedYear;
    private String selectedMonth;
    private String selecteddayOfMonth;

    CalendarView.OnDateChangeListener calendarhandler = new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            selectedYear = String.valueOf(year);
            selectedMonth = String.valueOf(month - 1);
            selecteddayOfMonth = String.valueOf(dayOfMonth);
        }
    };


    Button.OnClickListener saveHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(tryParse()) {
                String sBody = CreateRaw();
                SaveData(sBody);
                MainActivity ma = (MainActivity)getActivity();

            }
        }
    };

    Button.OnClickListener clearHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            tName.setText("");
            tTime.setText("");
            tDescription.setText("");
        }
    };

}
