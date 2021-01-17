package com.example.scaheadule;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Categories {
    private List<String> data;
    private List<String> customData;

    public Categories()
    {

    }

    //Load default and custom categories
    public List<String> LoadCategories(Context ctxt)
    {
        customData = new ArrayList<>();
        data = new ArrayList<>();

        //read from file
        InputStream inputStream = ctxt.getResources().openRawResource(R.raw.default_categories);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputReader);
        String line;

        //get strings from file
        try{
            while((line = buffReader.readLine()) != null)
            {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        inputStream = null;
        try {
            inputStream = ctxt.openFileInput("category_storage.txt");
            if(inputStream != null)
            {
                inputReader = new InputStreamReader(inputStream);
                buffReader = new BufferedReader(inputReader);

                //get strings from file
                try{
                    while((line = buffReader.readLine()) != null)
                    {
                        StringBuilder text = new StringBuilder();
                        text.append(line);
                        data.add(text.toString());
                        customData.add(text.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<String> getCustomData() {
        return customData;
    }

}
