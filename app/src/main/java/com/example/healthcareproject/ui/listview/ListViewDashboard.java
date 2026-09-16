package com.example.healthcareproject.ui.listview;

import java.util.HashMap;
import java.util.Map;

public class ListViewDashboard {

    public Map<String, String> item = new HashMap<>();

    public String first_name;
    public String last_name;
    public String Visit_Completed;
    public String company_name;

    public String Language_Name;
    public int Followup_Count;

    public ListViewDashboard() {
        item = new HashMap<>();
    }

    public void setItem(String key, String value){
        item.put(key,value);
    }

    public Map<String, String> getItem(){
        return item;
    }

    public void add(String key, String value) {
        item.put(key,value);
    }

    public String getValue(String key) {
        return item.get(key);
    }

    public void setFirstName(String first_name){
        this.first_name = first_name;
    }
    public String getFirstName(){
        return first_name;
    }
    public void setLastName(String last_name){
        this.last_name = last_name;
    }
    public String getLastName(){
        return last_name;
    }
    public void setVisitCompleted(String Visit_Completed){
        this.Visit_Completed = Visit_Completed;
    }
    public String getVisitCompleted(){
        return Visit_Completed;
    }

    public void setCompanyName(String company_name){
        this.company_name = company_name;
    }

    public String getCompanyName(){
        return company_name;
    }

    public void setLanguageName(String Language_Name){
        this.Language_Name = Language_Name;
    }
    public String getLanguageName(){
        return Language_Name;
    }

    public void setFollowUpCount(int Followup_Count){
        this.Followup_Count = Followup_Count;
    }
    public int getFollowUpCount(){
        return Followup_Count;
    }

}
