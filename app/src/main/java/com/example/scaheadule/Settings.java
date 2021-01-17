package com.example.scaheadule;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scaheadule.Adapters.ListAdapterCategory;
import com.example.scaheadule.Adapters.ListAdapterwCheck;
import com.example.scaheadule.Schedule.Scdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    View view;
    RecyclerView rCategory;
    Button bAdd;
    Button bDelete;
    CheckBox cDarkmode;

    List<String> categories;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        initialiseUI();
        return view;
    }

    private void initialiseUI()
    {
        rCategory = view.findViewById(R.id.recyclerCategory);
        bAdd = view.findViewById(R.id.btnAdd);
        bDelete = view.findViewById(R.id.btnDelete);
        cDarkmode = view.findViewById(R.id.chkDark);

        MainActivity ma = (MainActivity)getActivity();

        categories = new ArrayList<>();

        if(ma.getCustomCategories() != null) {
            categories.addAll(ma.getCustomCategories());
            ListAdapterCategory adapter = new ListAdapterCategory(createCategoryList());
            rCategory.setHasFixedSize(true);
            rCategory.setLayoutManager(new LinearLayoutManager(view.getContext()));
            rCategory.setAdapter(adapter);
        }

        bAdd.setOnClickListener(addHandler);
        bDelete.setOnClickListener(deleteHandler);
        cDarkmode.setOnCheckedChangeListener(dlHandler);
    }

    //Create list to use in ListAdapter
    public HashMap<Integer, String> createCategoryList() //pure
    {
        HashMap<Integer, String> result = new HashMap<>();
        int i = 0;
        for (String s : categories)
        {
            result.put(i, s);
            i++;
        }
        return result;
    }

    private void SaveNewCategories(String category)
    {
        StringBuilder text = new StringBuilder();
        for (String s : categories)
        {
            text.append(s);
            text.append('\n');
        }
        String sBody = text.toString();

        String sFileName = "category_storage.txt";
        File root = new File(sFileName);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(view.getContext().openFileOutput(sFileName, Context.MODE_PRIVATE));
            writer.append(sBody);
            writer.append(category);
            writer.flush();
            writer.close();
            Toast.makeText(view.getContext(), "Added Categories.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Refresh();
    }

    private String newCategory;
    private void AddCategory()
    {
        newCategory = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Add Custom Category");

        // Set up the input
        final EditText input = new EditText(view.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newCategory = input.getText().toString();
                SaveNewCategories(newCategory);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void DeleteCategories(ListAdapterCategory lac)
    {
        List<String> temp = categories;
        for (String s : lac.getSelectedCategories())
        {
            temp.remove(s);
        }

        StringBuilder text = new StringBuilder();
        for (String s : temp)
        {
            text.append(s);
            text.append('\n');
        }
        String sBody = text.toString();

        String sFileName = "category_storage.txt";
        File root = new File(sFileName);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(view.getContext().openFileOutput(sFileName, Context.MODE_PRIVATE));
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(view.getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Refresh();
    }

    private void Refresh()
    {
        MainActivity ma = (MainActivity)getActivity();
        ma.getCategories();

        if(ma.getCustomCategories() != null) {
            categories.addAll(ma.getCustomCategories());
            ListAdapterCategory adapter = new ListAdapterCategory(createCategoryList());
            rCategory.setHasFixedSize(true);
            rCategory.setLayoutManager(new LinearLayoutManager(view.getContext()));
            rCategory.setAdapter(adapter);
        }
    }

    CheckBox.OnCheckedChangeListener dlHandler = new CheckBox.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    };

    Button.OnClickListener addHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            AddCategory();
        }
    };

    Button.OnClickListener deleteHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            final ListAdapterCategory lac = (ListAdapterCategory)rCategory.getAdapter();
            if(!categories.isEmpty() && !lac.getSelectedCategories().isEmpty())
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("Are you sure you want to delete the selected categories?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteCategories(lac);
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog describer = builder1.create();
                describer.show();
            }
            else
            {
                Toast.makeText(view.getContext(), "No categories to delete.", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
