package com.example.scaheadule;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scaheadule.ImportEvents.ExamTimeTable;
import com.example.scaheadule.Adapters.ListAdapterwCheck;
import com.example.scaheadule.Schedule.Scdate;
import com.example.scaheadule.Schedule.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Record extends Fragment {
    View view;
    RecyclerView listViewer;
    TextView tTotal;
    Button bDelete;
    Button bRefresh;
    Button bReset;
    Button bImport;

    Scheduler sch;

    public Record() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_record, container, false);
        initialiseUI();
        return view;
    }

    //Create list to use in ListAdapter
    private HashMap<Integer, Scdate> createScdateList() //pure
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

    private void initialiseUI()
    {
        listViewer = view.findViewById(R.id.recyclerView);
        tTotal = view.findViewById(R.id.txtTotalEvents);
        bDelete = view.findViewById(R.id.btnDelete);
        bRefresh = view.findViewById(R.id.btnRefresh);
        bImport = view.findViewById(R.id.btnImportExam);
        bReset = view.findViewById(R.id.btnReset);

        MainActivity ma = (MainActivity)getActivity();
        sch = ma.loadScheduler();

        ListAdapterwCheck adapter = new ListAdapterwCheck(createScdateList());
        listViewer.setHasFixedSize(true);
        listViewer.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listViewer.setAdapter(adapter);

        tTotal.setText(String.valueOf(sch.getFinalData().size()));
        bDelete.setOnClickListener(deleteHandler);
        bRefresh.setOnClickListener(refreshHandler);
        bReset.setOnClickListener(resetHandler);
        bImport.setOnClickListener(importHandler);
    }

    private void Refresh()
    {
        MainActivity ma = (MainActivity)getActivity();
        ma.updateScheduler();
        sch = ma.loadScheduler();
        ListAdapterwCheck adapter = new ListAdapterwCheck(createScdateList());
        listViewer.setAdapter(adapter);
        tTotal.setText(String.valueOf(sch.getFinalData().size()));
    }

    private void ResetEvents()
    {
        String sFileName = "schedule_storage.txt";
        File root = new File(sFileName);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(view.getContext().openFileOutput(sFileName, Context.MODE_PRIVATE));
            writer.append("");
            writer.flush();
            writer.close();
            Toast.makeText(view.getContext(), "Reset Successful", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Refresh();
    }

    //Get selected scdates in listadapter, create new finaldata,
    //store it into the main sch data and then save
    private void DeleteEvents(ListAdapterwCheck lac)
    {
        List<Scdate> temp = sch.getFinalData();
        for (Scdate sc : lac.getSelectedSc())
        {
            temp.remove(sc);
        }

        StringBuilder text = new StringBuilder();
        sch.setFinalData(temp);
        for (String s : sch.rawDataSorted())
        {
            text.append(s);
            text.append('\n');
        }
        String sBody = text.toString();

        String sFileName = "schedule_storage.txt";
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


    //Builds the examtimetable data from ImportBrowser into Scdate
    //and then saves it using the same format as DeleteEvents()
    private void BuildImportData(List<ExamTimeTable> examTimeTables)
    {
        List<Scdate> temp = sch.getFinalData();
        for (ExamTimeTable ett : examTimeTables)
        {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                Date time = timeFormat.parse(ett.exTime);
                String ttime = timeFormat.format(time);

                Date date = dateFormat.parse(ett.exDate);

                Scdate sc = new Scdate(ett.exunitCode, date, ttime, "Exam",
                        ett.exCentre + "/" + ett.exLocation + "/" + ett.exBuilding + " Room: " + ett.exRoomName + " Seat No: " + ett.exSeatNo);
                temp.add(sc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        StringBuilder text = new StringBuilder();
        sch.setFinalData(temp);
        for (String s : sch.rawDataSorted())
        {
            text.append(s);
            text.append('\n');
        }
        String sBody = text.toString();

        String sFileName = "schedule_storage.txt";
        File root = new File(sFileName);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(view.getContext().openFileOutput(sFileName, Context.MODE_PRIVATE));
            writer.append(sBody);

            writer.flush();
            writer.close();
            Toast.makeText(view.getContext(), "Import Successful.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Gets imported data via index from ImportBrowser
    private List<ExamTimeTable> GetImportedData(Intent data)
    {
        Bundle importData = data.getExtras();
        List<ExamTimeTable> examTimeTables = new ArrayList<>();
        for(int i = 0; i < data.getIntExtra("examCount", 0); i++)
        {
            ExamTimeTable ett = importData.getParcelable(String.valueOf(i));
            examTimeTables.add(ett);
        }
        return examTimeTables;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            BuildImportData(GetImportedData(data));
            Refresh();
        }
   }

    Button.OnClickListener refreshHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Refresh();
        }
    };

    Button.OnClickListener resetHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
            builder1.setMessage("Are you sure you want to reset all events?");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ResetEvents();
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
    };

    Button.OnClickListener deleteHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            final ListAdapterwCheck lac = (ListAdapterwCheck)listViewer.getAdapter();
            if(!lac.getSelectedSc().isEmpty() && listViewer.getAdapter().getItemCount() != 0) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("Are you sure you want to delete the selected events?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteEvents(lac);
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
                Toast.makeText(view.getContext(), "No events to delete.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    Button.OnClickListener importHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(view.getContext(), ImportBrowser.class);
            startActivityForResult(intent, 123);
        }
    };



}
