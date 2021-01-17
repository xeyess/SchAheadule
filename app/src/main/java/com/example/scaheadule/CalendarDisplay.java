package com.example.scaheadule;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.scaheadule.Old.CreateEvent;
import com.example.scaheadule.Schedule.Scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarDisplay extends Fragment {

    View view;
    CalendarView tCalendar;
    Spinner tEventNames;
    EditText tStartDate;
    EditText tTimes;
    TextView tCategory;
    TextView tDescription;

    Button addButton;
    SimpleDateFormat dateFormat;

    Scheduler sch;
    List<String> categories;


    public CalendarDisplay() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        initialiseUI();
        return view;
    }

    public void initialiseUI()
    {
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        tCalendar = view.findViewById(R.id.calendarView);
        tEventNames = view.findViewById(R.id.spinnerEvents);
        tStartDate = view.findViewById(R.id.editStart);
        tTimes = view.findViewById(R.id.editEnd);
        tCategory = view.findViewById(R.id.editCategory);
        tDescription = view.findViewById(R.id.editDesc);
        addButton = view.findViewById(R.id.btnAdd);

        MainActivity ma = (MainActivity)getActivity();
        sch = ma.loadScheduler();

        addButton.setOnClickListener(btnHandler);
        tEventNames.setOnItemSelectedListener(spinnerHandler);
        tCalendar.setOnDateChangeListener(handler);

        Calendar c = Calendar.getInstance();
        //String currentDateString = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        updateInformationViaDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    //Displays the date according to the index of the data
    //Adds a linebreak depending on the length of the description
    private void displayDateData(int index)
    {
        tStartDate.setText(dateFormat.format(sch.getStartDate(index)));
        tTimes.setText(sch.getTime(index));
        tCategory.setText(sch.getCategory(index));
        if(sch.getDescription(index).length() > 30)
        {
            StringBuilder description = new StringBuilder();
            String[] longDesc = sch.getDescription(index).split(" ");
            int i = 0;
            for (String s : longDesc)
            {
                description.append(s);
                if(i == 1 || i == 3)
                {
                    description.append("\n");
                }
                i++;
            }
            tDescription.setText(description.toString());
        }
        else {
            tDescription.setText(sch.getDescription(index));
        }
    }

    Spinner.OnItemSelectedListener spinnerHandler = new Spinner.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String name = tEventNames.getSelectedItem().toString();
            if(!name.equals(""))
            {
                //gets the index from the name of the spinner item *may cause conflict
                int index = sch.getIndexFromName(name);
                if(index > -1 && index < sch.getEventNames().size())
                displayDateData(index);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //Selects the index of the event that is most accurate according to the current time
    //if the date in question is 'today'
    private int getClosestEvent(List<String> events)
    {
        Date currentDate = Calendar.getInstance().getTime();
        if(date.getDay() == currentDate.getDay() && date.getMonth() == currentDate.getMonth() && date.getYear() == currentDate.getYear())
        {
            int i = 0;
            for (String s : events)
            {
                int index = sch.getIndexFromName(s);
                try {
                    Date time = new SimpleDateFormat("HH:mm").parse(sch.getTime(index));
                    Calendar t = Calendar.getInstance();
                    t.setTime(time);
                    if(t.getTime().after(currentDate))
                    {
                        return i;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        else {
            return 0;
        }
        return 0;
    }

    //Displays the current date information relative to the selected date
    public void updateInformationViaDate(int year, int month, int dayOfMonth)
    {
        try {
            date = dateFormat.parse(dayOfMonth + "-" + (month + 1) + "-" + year);
            int index = 0;
            List<String> events = new ArrayList<>();
            for (Date d1 : sch.getStartDates())
            {
                if(date.compareTo(d1) == 0)
                {
                    events.add(sch.getEventName(index));
                }
                else
                {
                    tStartDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    tTimes.setText("00:00");
                    tCategory.setText("N/A");
                    tDescription.setText("N/A");
                }
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, events);
            tEventNames.setAdapter(arrayAdapter);
            tEventNames.setSelection(getClosestEvent(events));
        } catch (Exception e) {
            //Toast.makeText(view.getContext(), "Could not load data.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Date values to get CalenderView info
    private Date date;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    CalendarView.OnDateChangeListener handler = new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            selectedMonth = month;
            selectedYear = year;
            selectedDay = dayOfMonth;
            updateInformationViaDate(year, month, dayOfMonth);
        }
    };

    //Show CreateEvent Activity to add new event
    private void AddNewEvent()
    {
        Intent intent = new Intent(view.getContext(), CreateEvent.class);

        //Entail final sorted raw data into string to save
        StringBuilder text = new StringBuilder();
        for (String s : sch.rawDataSorted())
        {
            text.append(s);
            text.append('\n');
        }
        String sBody = text.toString();
        intent.putExtra("raw", sBody);

        //pass date info
        intent.putExtra("date",date.getTime());
        intent.putExtra("year", selectedYear);
        intent.putExtra("month", selectedMonth);
        intent.putExtra("day", selectedDay);

        startActivityForResult(intent, 123);
    }

    Button.OnClickListener btnHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            AddNewEvent();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            UpdateChanges();
        }
    }

    //Update the changes to the main source in MainActivity
    public void UpdateChanges()
    {
        MainActivity ma = (MainActivity)getActivity();
        ma.updateScheduler();
        sch = ma.loadScheduler();
        updateInformationViaDate(selectedYear, selectedMonth, selectedDay);
    }

}
