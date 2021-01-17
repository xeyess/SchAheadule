package com.example.scaheadule.Old;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scaheadule.Categories;
import com.example.scaheadule.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateEvent extends AppCompatActivity {
    View view;
    EditText tName;
    EditText tTime;
    Spinner tCategory;
    CalendarView tCalendar;
    EditText tDescription;

    Button clearButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initialiseUI();
    }

    private void initialiseUI()
    {
        tName = findViewById(R.id.editName);
        tTime = findViewById(R.id.editTime);
        tCalendar = findViewById(R.id.calendarAdd);
        tCategory = findViewById(R.id.spinnerCategory);
        tDescription = findViewById(R.id.editDescription);

        //Load Categories
        Categories cg = new Categories();
        List<String> categories = cg.LoadCategories(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        tCategory.setAdapter(arrayAdapter);

        saveButton = findViewById(R.id.btnSave);
        clearButton = findViewById(R.id.btnClear);

        tCalendar.setOnDateChangeListener(calendarhandler);
        saveButton.setOnClickListener(saveHandler);
        clearButton.setOnClickListener(clearHandler);

        //set date to CalendarDisplay selected date
        Calendar calendar = Calendar.getInstance();

        Long gotDate = getIntent().getLongExtra("date", tCalendar.getDate());
        tCalendar.setDate(gotDate);

        selectedYear = String.valueOf(getIntent().getIntExtra("year", calendar.get(Calendar.YEAR)));
        selectedMonth = String.valueOf(getIntent().getIntExtra("month", calendar.get(Calendar.MONTH)) + 1);
        selecteddayOfMonth = String.valueOf(getIntent().getIntExtra("day", calendar.get(Calendar.DAY_OF_MONTH)));

        //selectedYear = String.valueOf(calendar.get(Calendar.YEAR));
        //selectedMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        //selecteddayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    }

    //Ensures all required fields are filled and date is parseable
    private Boolean tryParse()
    {
        Boolean result = false;
        if (tName.getText().toString().isEmpty() || tTime.getText().toString().isEmpty() || tDescription.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Please make sure all fields are filled.", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Date time1 = new SimpleDateFormat("HH:mm").parse(tTime.getText().toString());
                result = true;

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Make sure that all fields are filled out correctly.", Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }

    //Entails the form information into a useable line to be saved
    private String GenerateLine()
    {
        String result = "";
        String resultDate = selecteddayOfMonth + "-" + selectedMonth + "-" + selectedYear;
        result = resultDate + "<>" + tTime.getText().toString() + "<>" + tName.getText().toString() + "<>" + tCategory.getSelectedItem().toString() + "<>"
                + tDescription.getText().toString();
        return result;
    }

    //Saves the data to a file in local
    private void SaveData(String sBody)
    {
        try {
            String sFileName = "schedule_storage.txt";
            File root = new File(sFileName);
            if (!root.exists()) {
                root.mkdirs();
            }

            OutputStreamWriter writer = new OutputStreamWriter(openFileOutput(sFileName, Context.MODE_PRIVATE));
            writer.append(sBody);
            writer.append(GenerateLine());
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved @" + root.getCanonicalPath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Date values to use CalenderView info
    private String selectedYear;
    private String selectedMonth;
    private String selecteddayOfMonth;
    CalendarView.OnDateChangeListener calendarhandler = new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            selectedYear = String.valueOf(year);
            selectedMonth = String.valueOf(month + 1);
            selecteddayOfMonth = String.valueOf(dayOfMonth);
        }
    };


    Button.OnClickListener saveHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(tryParse()) {
                String sBody = getIntent().getStringExtra("raw");
                SaveData(sBody);
                //most recent save will always be last, every time data saved the rest are sorted
                //MainActivity ma = (MainActivity)getActivity();
                setResult(RESULT_OK);
                finish();
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
