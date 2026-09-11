package com.example.healthcareproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormItem {

    private String type;
    private String label;
    private String hint;
    private String helperText;
    private boolean required;
    private String value;

    private String label2;
    private String hint2;
    private String value2;
    private boolean required2;

    private int layoutResId;
    private FieldBinder binder;

    private boolean error;
    private String validationMessage;
    private String validationMessage2;
    private int listIndex;
    private List<Integer> options2 = new ArrayList<>();
    private List<String> options = new ArrayList<>();
    private boolean isEnabled;

    public FormItem() {
    }

    public void setType(String type){ this.type = type; }
    public String getType(){ return type; }

    public void setLabel(String label){ this.label = label; }
    public String getLabel(){ return label; }

    public void setHint(String hint){ this.hint = hint; }
    public String getHint(){ return hint; }

    public void setHelperText(String helperText){ this.helperText = helperText; }
    public String getHelperText(){ return helperText; }

    public void setRequired(boolean required){ this.required = required; }
    public boolean isRequired(){ return required; }
    public void setRequired2(boolean required2){
        this.required2 = required2;
    }
    public boolean isRequired2(){
        return required2;
    }

    public void setValue(String value){ this.value = value; }
    public String getValue(){ return value; }

    public void setLabel2(String label2) { this.label2 = label2; }
    public String getLabel2() { return label2; }

    public void setHint2(String hint2) { this.hint2 = hint2; }
    public String getHint2() { return hint2; }

    public void setValue2(String value2) { this.value2 = value2; }
    public String getValue2() { return value2; }

    public void setOptions(List<String> options) { this.options = options; }
    public List<String> getOptions(){ return options; }

    public void setOptions2(List<Integer> options2) {
        this.options2 = options2;
    }

    public List<Integer> getOptions2() {
        return options2;
    }

    public void setError(boolean error){ this.error = error; }
    public boolean hasError(){ return error; }

    public void setValidationMessage(String validationMessage){ this.validationMessage = validationMessage; }
    public String getValidationMessage(){ return validationMessage; }

    public void setValidationMessage2(String validationMessage2) {
        this.validationMessage2 = validationMessage2;
    }

    public String getValidationMessage2() {
        return validationMessage2;
    }

    public void setLayoutResId(int layoutResId){ this.layoutResId = layoutResId; }
    public int getLayoutResId(){ return layoutResId; }

    public void setBinder(FieldBinder binder){ this.binder = binder; }
    public FieldBinder getBinder() { return binder; }

    public void setListIndex(int listIndex){ this.listIndex = listIndex; }
    public int getListIndex(){ return listIndex; }

    public enum GroupPosition{TOP, BOTTOM, MIDDLE, SINGLE}
    private GroupPosition groupPosition;

    public void setGroupPosition(GroupPosition groupPosition) { this.groupPosition = groupPosition; }
    public GroupPosition getGroupPosition() { return groupPosition; }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
}