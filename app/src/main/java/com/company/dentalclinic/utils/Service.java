package com.company.dentalclinic.utils;


import java.io.Serializable;

public class Service implements Serializable {
    private String name;
    private int price;
    private int duration;

    public Service(String name, int price, int doctor) {
        this.name = name;
        this.price = price;
        this.duration = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
