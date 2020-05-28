package com.example.Business_Layer;

import java.sql.Time;
import java.util.Date;

public class Intake {
int id;
    String name;
    Date date;
    Time time;
    boolean check;
    public Intake(String name, Date date,Time time) {
        this.name = name;
        this.date = date;
        this.time=time;
        this.check=false;
    }
}
