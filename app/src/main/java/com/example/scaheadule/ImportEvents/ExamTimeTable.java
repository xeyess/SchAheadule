package com.example.scaheadule.ImportEvents;

import android.os.Parcel;
import android.os.Parcelable;

public class ExamTimeTable implements Parcelable {
    public String exStatus;
    public String exDate;
    public String exDay;
    public String exTime;
    public String exunitCode;
    public String exsTitle;
    public String exNo;
    public String exTitle;
    public String exDuration;
    public String exReadingTime;
    public String exSDuration;
    public String exCentre;
    public String exLocation;
    public String exBuilding;
    public String exRoomID;
    public String exRoomName;
    public String exSeatNo;

    //Parcelable ExamTimeTable with all values
    public ExamTimeTable(String exStatus, String exDate, String exDay, String exTime, String exunitCode, String exsTitle, String exNo, String exTitle, String exDuration, String exReadingTime, String exSDuration
            ,String exCentre, String exLocation, String exBuilding, String exRoomID, String exRoomName, String exSeatNo)
    {
        this.exStatus = exStatus;
        this.exDate = exDate;
        this.exDay = exDay;
        this.exTime = exTime;
        this.exunitCode = exunitCode;
        this.exsTitle = exsTitle;
        this.exNo = exNo;
        this.exTitle = exTitle;
        this.exDuration = exDuration;
        this.exReadingTime = exReadingTime;
        this.exSDuration = exSDuration;
        this.exCentre = exCentre;
        this.exLocation = exLocation;
        this.exBuilding = exBuilding;
        this.exRoomID = exRoomID;
        this.exRoomName = exRoomName;
        this.exSeatNo = exSeatNo;
    }

    protected ExamTimeTable(Parcel in) {
        exStatus = in.readString();
        exDate = in.readString();
        exDay = in.readString();
        exTime = in.readString();
        exunitCode = in.readString();
        exsTitle = in.readString();
        exNo = in.readString();
        exTitle = in.readString();
        exDuration = in.readString();
        exReadingTime = in.readString();
        exSDuration = in.readString();
        exCentre = in.readString();
        exLocation = in.readString();
        exBuilding = in.readString();
        exRoomID = in.readString();
        exRoomName = in.readString();
        exSeatNo = in.readString();
    }

    public static final Creator<ExamTimeTable> CREATOR = new Creator<ExamTimeTable>() {
        @Override
        public ExamTimeTable createFromParcel(Parcel in) {
            return new ExamTimeTable(in);
        }

        @Override
        public ExamTimeTable[] newArray(int size) {
            return new ExamTimeTable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exStatus);
        dest.writeString(exDate);
        dest.writeString(exDay);
        dest.writeString(exTime);
        dest.writeString(exunitCode);
        dest.writeString(exsTitle);
        dest.writeString(exNo);
        dest.writeString(exTitle);
        dest.writeString(exDuration);
        dest.writeString(exReadingTime);
        dest.writeString(exSDuration);
        dest.writeString(exCentre);
        dest.writeString(exLocation);
        dest.writeString(exBuilding);
        dest.writeString(exRoomID);
        dest.writeString(exRoomName);
        dest.writeString(exSeatNo);
    }
}