package com.example.scaheadule.Schedule;

import android.content.Context;

import com.example.scaheadule.R;
import com.example.scaheadule.Schedule.Scdate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Scheduler {

    //data variables

    private List<String> eventNames;
    private List<Date> startDates;
    private List<String> times;
    private List<String> categories;
    private List<String> descriptions;

    private List<String> rawData;

    private List<Scdate> finalData;

    //loads local or raw set data depending on inputStream (existence)
    public void LoadData(Context ctxt)
    {
        Boolean exists = false;
        try
        {
            InputStream inputStream = ctxt.openFileInput("schedule_storage.txt");
            exists = inputStream != null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(exists)
        {
            UpdateData(ctxt);
        }
        else
        {
            ReadData(ctxt);
        }


    }

    //Update data to load via local
    public void UpdateData(Context ctxt)
    {
        InputStream inputStream = null;
        try {
            inputStream = ctxt.openFileInput("schedule_storage.txt");
            if(inputStream!=null)
            {
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;

                //get strings from file
                rawData = new ArrayList<>();
                try{
                    while((line = buffReader.readLine()) != null)
                    {
                        rawData.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                StoreDataToMemory();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        createFinalData();
        finalData = orderDatabyDate(finalData);

    }

    //Read data from raw
    public void ReadData(Context ctxt){
        //read from file
        InputStream inputStream = ctxt.getResources().openRawResource(R.raw.schedule_storage);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputReader);
        String line;

        //get strings from file
        rawData = new ArrayList<>();
        try{
            while((line = buffReader.readLine()) != null)
            {
                rawData.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StoreDataToMemory();
        createFinalData();
        finalData = orderDatabyDate(finalData);

    }

    //store the information into the Scheduler object
    private void StoreDataToMemory()
    {
        //initialise variables
        eventNames = new ArrayList<>();
        startDates = new ArrayList<>();
        times = new ArrayList<>();
        categories = new ArrayList<>();
        descriptions = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        //assign data from string
        for (String s : rawData)
        {
            String[] scheduleData;
            scheduleData = s.split("<>");
            try {
                startDates.add(dateFormat.parse(scheduleData[0]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            times.add(scheduleData[1]);
            eventNames.add(scheduleData[2]);
            categories.add(scheduleData[3]);
            descriptions.add(scheduleData[4]);
        }
    }

    //Create a list of Scdate for use in other features from
    //the raw data extracted
    public void createFinalData()
    {
        finalData = new ArrayList<>();
        int i = 0;
        for(String s : eventNames)
        {
            finalData.add(new Scdate(eventNames.get(i), startDates.get(i), times.get(i), categories.get(i), descriptions.get(i)));
            i++;
        }
    }

    //Get the index of an item from the name
    public int getIndexFromName(String name)
    {
        Boolean found = false;
        int i = 0;
        for (String s : eventNames)
        {
            if(s.equals(name))
            {
                found = true;
                break;
            }
            i++;
        }

        if(found)
        {
            return i;
        }
        else
        {
            return -1;
        }
    }

    //Gets rawdata back from the sorted final data for saving
    //*when adding new data, it cannot save as sorted but is here instead
    public List<String> rawDataSorted()
    {
        List<String> result = new ArrayList<>();
        for(Scdate sc : finalData)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String a = dateFormat.format(sc.getStartDate());
            String b = sc.getTime();
            String c = sc.getEventName();
            String d = sc.getCategory();
            String e = sc.getDescription();
            result.add(a + "<>" + b + "<>" + c + "<>" + d + "<>" + e);
        }
        return result;
    }

    //Get the dateTime (combined date and time) to sort
    public Date getDateTime(List<Scdate> data, int j)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();

        try {
            Date timef = timeFormat.parse(data.get(j).getTime());
            Date datef = data.get(j).getStartDate();

            c1.setTime(timef);
            c2.setTime(datef);
            c3.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE), c1.get(Calendar.HOUR), c1.get(Calendar.MINUTE));
            return c3.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Sort via selection sort
    public List<Scdate> orderDatabyDate(List<Scdate> data)
    {
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < data.size() - 1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < data.size(); j++)
                if (getDateTime(data,j).before(getDateTime(data,min_idx)))
                    min_idx = j;

            //if (data.get(j).getStartDate().before(data.get(min_idx).getStartDate()))
            //    min_idx = j;

            // Swap the found minimum element with the first
            // element
            Scdate temp = data.get(min_idx);
            data.set(min_idx, data.get(i));
            data.set(i, temp);
        }
        return data;
    }


    public List<String> getEventNames() {
        return eventNames;
    }

    public List<Date> getStartDates() {
        return startDates;
    }
    public List<String> getTimes() {
        return times;
    }
    public List<String> getCategories() {
        return categories;
    }

    public String getEventName(int i) {
        return eventNames.get(i);
    }

    public Date getStartDate(int i) {
        return startDates.get(i);
    }

    public String getTime(int i) {
        return times.get(i);
    }

    public String getCategory(int i) {
        return categories.get(i);
    }

    public String getDescription(int i) {
        return descriptions.get(i);
    }

    public List<String> getRawData() {
        return rawData;
    }

    public List<Scdate> getFinalData() {
        return finalData;
    }

    public void setFinalData(List<Scdate> finalData) {
        this.finalData = finalData;
    }

}
