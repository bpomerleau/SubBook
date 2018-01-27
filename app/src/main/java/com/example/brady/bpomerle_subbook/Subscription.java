package com.example.brady.bpomerle_subbook;

import java.util.Date;

/**
 * Created by Brady on 2018-01-26.
 */

public class Subscription {

    private String name;
    private Date date;
    private float amount;
    private String comment;

    public Subscription(String name, Date date, float amount){
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.comment = "";
    }

    public Subscription(String name, Date date, float amount, String comment){
        this(name, date, amount);
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public float getAmount(){
        return amount;
    }

    public void setAmount(float amount){
        this.amount = amount;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

}
