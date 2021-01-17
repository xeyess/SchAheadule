package com.example.scaheadule.ImportEvents;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.example.scaheadule.ImportBrowser;

import java.util.ArrayList;
import java.util.List;

//JavaScriptInterface
public class JavaInterpreter {
    private Context ctxt;
    public String data;
    public String htmlData;
    public List<ExamTimeTable> examTimeTable;

    public JavaInterpreter(Context ctxt)
    {
        this.ctxt = ctxt;
    }

    //gets html content and ensures that it is a timetable
    @JavascriptInterface
    public void processContent(String aContent)
    {
        data = aContent;
        if(data.contains("Exam Period:"))
        {
            examTimeTable = ExamTimeTableBuilder(data);
        }

    }

    //creates the timetable via html content and splits them into useable data
    //inspection of html code using developer tools ensure that they give proper data
    public List<ExamTimeTable> ExamTimeTableBuilder(String data)
    {
        List<ExamTimeTable> result = new ArrayList<>();
        String[] raws = data.split("\n");
        String[] timetableInfo;
        int index = 0;
        int timeTables = 0;
        for(String s : raws)
        {
            if(s.contains("View\t"))
            {
                timetableInfo = s.split("\t");
                result.add(new ExamTimeTable(timetableInfo[1],timetableInfo[2],timetableInfo[3],timetableInfo[4],timetableInfo[5],timetableInfo[6]
                        ,timetableInfo[7],timetableInfo[8],timetableInfo[9],timetableInfo[10],timetableInfo[11],timetableInfo[12],timetableInfo[13]
                        ,timetableInfo[14],timetableInfo[16],timetableInfo[17],timetableInfo[18]));
                timeTables++;
            }
            index++;
        }
        return result; //exit title
    }
}
