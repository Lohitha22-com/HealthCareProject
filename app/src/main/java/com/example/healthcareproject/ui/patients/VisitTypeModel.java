package com.example.healthcareproject.ui.patients;

public class VisitTypeModel {
    private String visitType;
    private int visitTypeId;

    VisitTypeModel(int visitTypeId, String visitType){
       this.visitTypeId = visitTypeId;
       this.visitType = visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitTypeId(int visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public int getVisitTypeId() {
        return visitTypeId;
    }
}
