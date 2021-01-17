package com.example.scaheadule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.scaheadule.ImportEvents.Browser;
import com.example.scaheadule.ImportEvents.ExamTimeTable;
import com.example.scaheadule.ImportEvents.JavaInterpreter;

import java.util.List;

public class ImportBrowser extends AppCompatActivity {
    Context ctxt;
    WebView wv;
    Button bImport;
    public String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_browser);
        initialiseUI();
    }

    //Make retrievable JavaScriptInterface
    JavaInterpreter jI;
    private void initialiseUI()
    {
        wv = findViewById(R.id.webViewer);
        bImport = findViewById(R.id.btnImport);
        bImport.setOnClickListener(importHandler);

        ctxt = this;

        //Create WebView
        jI = new JavaInterpreter(this);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(jI, "INTERFACE");
        wv.setScrollBarStyle(wv.SCROLLBARS_INSIDE_OVERLAY);
        wv.setWebViewClient(new Browser());
        ExamTimeTableGet();
    }

    //Gets imported data from the JavaScriptInterface and stores each parceable exam
    //into the output intent via an index of i
    private void ParseImportData()
    {
        Intent output = new Intent();
        List<ExamTimeTable> importedTimeTable = jI.examTimeTable;
        output.putExtra("examCount",importedTimeTable.size());

        int i = 0;
        for(ExamTimeTable ett : importedTimeTable)
        {
            output.putExtra(String.valueOf(i), ett);
            i++;
        }
        setResult(RESULT_OK, output);
        finish();
    }

    Button.OnClickListener importHandler = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if(jI.examTimeTable != null)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ctxt);
                builder1.setMessage("Do you want to import this exam time table?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ParseImportData();
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
                Toast.makeText(ctxt, "Exam Time Table not detected.", Toast.LENGTH_LONG).show();
            }
        }
    };

    //URL of ExamTimeTable
    public void ExamTimeTableGet()
    {
        wv.loadUrl("https://s1.swin.edu.au/eStudent/SM/ExamTtable10.aspx?r=%23SU.EST.STUDENT&f=%23SU.EST.EXAMDTLS.WEB");
    }
}
