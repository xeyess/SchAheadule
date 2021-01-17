package com.example.scaheadule.Schedule;

import java.util.Date;

public class Scdate {

    private String eventName;
    private Date startDate;
    private String time;
    private String category;
    private String description;

    public Scdate(String eventName, Date startDate, String time, String category, String description) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.time = time;
        this.category = category;
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

}
