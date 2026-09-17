package com.example.healthcareproject.data.model;

import java.util.ArrayList;
import java.util.List;

public class MenuModelClass {

    List<MenuModelClass> children = new ArrayList<>();

    public int menu_id;
    public String id;
    public String title;
    public String type;
    public String url;
    public Integer parent_id;
    public int sort_order;


    //Using getters and setters to access the values

    public void setMenu_id(int menu_id){
        this.menu_id = menu_id;
    }
    public int getMenu_id(){
        return menu_id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return url;
    }
    public void setParent_id(Integer parent_id){
        this.parent_id = parent_id;
    }
    public Integer getParent_id(){
        return parent_id;
    }
    public void setSort_order(int sort_order){
        this.sort_order =sort_order;
    }
    public int getSort_order(){
        return sort_order;
    }

    //a getter and setter for accessing the sub menus
    public List<MenuModelClass> getChildren() {
        return children;
    }

    public void setChildren(List<MenuModelClass> children) {
        this.children = children;
    }

    // A getter and setter for the expandable list.

    public boolean isExpanded = false;

    public boolean isExpanded() {
        return isExpanded;
    }
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
