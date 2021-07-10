package com.company.dentalclinic.utils;

import java.io.Serializable;

public class Doctor implements Serializable {
    private int image;
    private String fullNаme;
    private String experience;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFullNаme() {
        return fullNаme;
    }

    public void setFullNаme(String fullNаme) {
        this.fullNаme = fullNаme;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
