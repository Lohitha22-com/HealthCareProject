package com.example.healthcareproject.ui.dashboard;

public class DashboardItem {

    private String title;
    private int logos;

    public DashboardItem() {}


    public DashboardItem(String title, int logos) {
        this.title = title;
        this.logos = logos;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getImage(){
        return logos;
    }

    public void setImage(int logos){
        this.logos = logos;
    }

}
