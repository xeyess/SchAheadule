package com.example.scaheadule.Old;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadData extends AsyncTask
{
    //initialise variables
    ProgressDialog progressDialog;
    Context ctxt;

    //constructor
    public LoadData(Context ctxt)
    {
        this.ctxt = ctxt;

        //progressbar
        progressDialog = new ProgressDialog(ctxt);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        progressDialog.setProgress(100);
    }

    @Override //executed, start showing loading
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override //progress is finished, close progressbar
    protected void onPostExecute(Object o) {
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //getScheduler();
        //getCategories();
        return null;
    }
}