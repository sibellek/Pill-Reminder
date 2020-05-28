package com.example.Business_Layer;

import java.sql.Date;
import java.sql.Time;

public class Medication {
    int id;
    String name;
    Time time;
    int skipDays;
    Date startDate;
    Date endDate;
    public Medication(int id,String name,Time time, int skipDays, Date startDate, Date endDate){
        this.id=id;
        this.name=name;
        this.time=time;
        this.skipDays=skipDays;
        this.startDate=startDate;
        this.endDate=endDate;
    }
}
