package com.example.healthcareproject;

import java.util.ArrayList;
import java.util.List;

public class FormItem {

    private String type;
    private String label;
    private String hint;
    private String helperText;
    private boolean required;
    private String value;

    private  int layoutResId;
    private FieldBinder binder;

    private boolean error;

    private String validationMessage;

    private  int listIndex;

    private List<String> options = new ArrayList<>();

    public FormItem() {
    }

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
    public void setHint(String hint){
        this.hint = hint;
    }
    public String getHint(){
        return hint;
    }
    public void setHelperText(String helperText){
        this.helperText = helperText;
    }
    public String getHelperText(){
        return helperText;
    }
    public void setRequired(boolean required){
        this.required = required;
    }
    public boolean isRequired(){
        return required;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
    public List<String> getOptions(){
        return options;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean hasError(){
        return error;
    }

    public void setValidationMessage(String validationMessage){
        this.validationMessage = validationMessage;
    }

    public String getValidationMessage(){
        return validationMessage;
    }

    public void setLayoutResId(int layoutResId){
        this.layoutResId = layoutResId;
    }
    public int getLayoutResId(){
        return layoutResId;
    }
    public void setBinder(FieldBinder binder){
        this.binder = binder;
    }

    public FieldBinder getBinder() {
        return binder;
    }

    public void setListIndex(int listIndex){
        this.listIndex = listIndex;
    }

    public int getListIndex(){
        return  listIndex;
    }

    public enum GroupPosition{TOP, BOTTOM, MIDDLE, SINGLE}
        ;
        private GroupPosition groupPosition;

        public void setGroupPosition(GroupPosition groupPosition) {
            this.groupPosition = groupPosition;
        }

        public GroupPosition getGroupPosition() {
            return groupPosition;
        }
}
