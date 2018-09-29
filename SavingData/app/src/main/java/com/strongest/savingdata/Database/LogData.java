package com.strongest.savingdata.Database;

public class LogData {

    public String full;
    public String date;
    public String time;

    public LogData(String full, String date, String time){
        this.full = full;
        this.date = date;
        this.time = time;
    }

    public static class LogDataSets{

        public String title;
        public String rep;
        public String rest;
        public double weight;
        public boolean add;

        public LogDataSets(String title, String rep, String rest, double weight){

            this.title = title;
            this.rep = rep;
            this.rest = rest;
            this.weight = weight;
        }

    }
}
