package com.example.scaheadule;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.scaheadule.Adapters.ListAdapter;
import com.example.scaheadule.Schedule.Scdate;
import com.example.scaheadule.Schedule.Scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingEvent extends Fragment {
    View view;
    Spinner sFilter;
    RecyclerView listViewer;
    Button bFilter;
    Button bRefresh;

    Scheduler sch;
    List<String> categories;


    public UpcomingEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upcoming_event, container, false);
        initialiseUI();
        return view;

    }

    public void initialiseUI()
    {
        MainActivity ma = (MainActivity)getActivity();
        sch = ma.loadScheduler();

        sFilter = view.findViewById(R.id.spinnerfCategory);
        categories = new ArrayList<>();
        categories.add("All");
        categories.addAll(ma.loadCategories());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        sFilter.setAdapter(arrayAdapter);

        listViewer = view.findViewById(R.id.recyclerUpcoming);
        ListAdapter adapter = new ListAdapter(upcomingWeek("All"));
        listViewer.setHasFixedSize(true);
        listViewer.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listViewer.setAdapter(adapter);

        bFilter = view.findViewById(R.id.btnFilter);
        bFilter.setOnClickListener(filterHandler);

        bRefresh = view.findViewById(R.id.btnRefresh);

    }

    //Create list for use with ListAdapter
    public HashMap<Integer, Scdate> createScdateList() //pure
    {
        HashMap<Integer, Scdate> result = new HashMap<>();
        int i = 0;
        for (Scdate sc : sch.getFinalData())
        {
            result.put(i, sc);
            i++;
        }
        return result;
    }

    //Gets list of Scdate for use of ListAdapter if they are in the same week
    public HashMap<Integer, Scdate> upcomingWeek(String filter) //pure
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        HashMap<Integer, Scdate> result = new HashMap<>();
        int i = 0;
        for (Scdate sc : sch.getFinalData())
        {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(sc.getStartDate());

            if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                    && c1.get(Calendar.WEEK_OF_MONTH) == c2.get(Calendar.WEEK_OF_MONTH)
                    ) {
                if(filter.equals("All")) {
                    result.put(i, sc);
                    i++;
                }
                else
                {
                    if(sc.getCategory().compareTo(filter) == 0)
                    {
                        result.put(i, sc);
                        i++;
                    }
                }
            }
        }
        return result;
    }

    Button.OnClickListener refreshHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            categories = new ArrayList<>();
            categories.add("All");

            MainActivity ma = (MainActivity)getActivity();
            ma.updateScheduler();

            categories.addAll(ma.loadCategories());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
            sFilter.setAdapter(arrayAdapter);

            ListAdapter adapter = new ListAdapter(upcomingWeek(sFilter.getSelectedItem().toString()));
            listViewer.setAdapter(adapter);
        }
    };

    Button.OnClickListener filterHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            MainActivity ma = (MainActivity)getActivity();
            ma.updateScheduler();
            ListAdapter adapter = new ListAdapter(upcomingWeek(sFilter.getSelectedItem().toString()));
            listViewer.setAdapter(adapter);
        }
    };

}
