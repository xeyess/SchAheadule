package com.example.scaheadule.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.scaheadule.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListAdapterCategory extends RecyclerView.Adapter<ListAdapterCategory.ViewHolder>{
    private HashMap<Integer, String> listdata;
    private List<String> selectedCategories = new ArrayList<>();

    // RecyclerView recyclerView;
    public ListAdapterCategory (HashMap<Integer, String> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String myListData = listdata.get(position);

        holder.tName.setText(listdata.get(position));
        holder.chkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.chkBox.isChecked())
                {
                    selectedCategories.add(listdata.get(position));
                }
                else
                {
                    selectedCategories.remove(listdata.get(position));
                }
            }
        });
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tName;
        public CheckBox chkBox;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tName = itemView.findViewById(R.id.textName);
            this.chkBox = itemView.findViewById(R.id.checkBox);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
