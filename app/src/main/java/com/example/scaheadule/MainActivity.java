package com.example.scaheadule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.scaheadule.Adapters.ViewPagerAdapter;
import com.example.scaheadule.Schedule.Scheduler;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Scheduler storedScheduler;
    private List<String> categories;
    private Categories cg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getScheduler();
        getCategories();

        initialiseFragments();
    }

    //Scheduler Methods
    public Scheduler loadScheduler()
    {
        return storedScheduler;
    }

    public void getScheduler()
    {
        storedScheduler = new Scheduler();
        storedScheduler.LoadData(this);
    }

    public void updateScheduler()
    {
        storedScheduler.UpdateData(this);
    }

    //Category Methods
    public List<String> loadCategories()
    {
        return categories;
    }

    public void getCategories()
    {
        cg = new Categories();
        categories = cg.LoadCategories(this);
    }

    public List<String> getCustomCategories()
    {
        return cg.getCustomData();
    }

    public void initialiseFragments()
    {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        FragmentManager fm = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(fm);

        adapter.AddFragment(new CalendarDisplay(), "Calendar");
        //adapter.AddFragment(new AddEvent(), "Add Event");
        adapter.AddFragment(new UpcomingEvent(), "Upcoming Events");
        adapter.AddFragment(new Record(), "Records");
        adapter.AddFragment(new Settings(), "Setting");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_calendar);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_upcoming);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_record);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);
    }


}
