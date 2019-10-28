package com.danex.zeitapp;

import java.sql.Time;
import java.util.Date;

public class ZeitMemo {

    private Date datum;
    private Time startTime;
    private Time endTime;

    public ZeitMemo(Date datum, Time startTime, Time endTime){
        this.datum = datum;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Date getDatum() {
        return datum;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString(){
        String output = datum+"|"+startTime+"|"+endTime;
        return output;
    }

}
