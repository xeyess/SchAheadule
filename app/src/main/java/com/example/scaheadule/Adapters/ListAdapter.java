package com.example.scaheadule.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.scaheadule.R;
import com.example.scaheadule.Schedule.Scdate;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private HashMap<Integer, Scdate> listdata;

    // RecyclerView recyclerView;
    public ListAdapter(HashMap<Integer, Scdate> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Scdate myListData = listdata.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        holder.tDate.setText(dateFormat.format(listdata.get(position).getStartDate()));
        holder.tTime.setText(listdata.get(position).getTime());
        holder.tName.setText(listdata.get(position).getEventName());

        /* Add Done depending on date
        Calendar c = Calendar.getInstance();
        if(c.getTime().after(listdata.get(position).getStartDate()))
        {
            holder.tName.setText(listdata.get(position).getEventName() + " (Done)");
        }
        else
        {
            holder.tName.setText(listdata.get(position).getEventName());
        }
        */

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("Task Description\n" + listdata.get(position).getDescription());
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog describer = builder1.create();
                describer.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tName;
        public TextView tDate;
        public TextView tTime;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tName = itemView.findViewById(R.id.textName);
            this.tDate = itemView.findViewById(R.id.textDate);
            this.tTime = itemView.findViewById(R.id.textTime);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
