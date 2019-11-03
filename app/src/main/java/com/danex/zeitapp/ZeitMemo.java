package com.danex.zeitapp;


public class ZeitMemo {

    private String datum;
    private String timeWorked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;


    public String getTimeWorked() {
        return timeWorked;
    }

    public void setTimeWorked(String timeWorked) {
        this.timeWorked = timeWorked;
    }

    public ZeitMemo(String Date, String timeWorked, long id){
        this.datum = Date;
        this.timeWorked = timeWorked;
        this.id = id;
    }
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }


    @Override
    public String toString(){
        String output = datum+"|"+timeWorked;
        return output;
    }

}
