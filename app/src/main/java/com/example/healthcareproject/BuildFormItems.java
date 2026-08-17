package com.example.healthcareproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BuildFormItems {

    private static class FieldConfig{
        String type, label, hint, helperText, validationMessage;
        boolean required;

        int layoutResId;

        FieldBinder binder;

        List<String> options;

        FieldConfig(String type, String label, String hint, String helperText, String validationMessage, boolean required, List<String> options){
            this.type = type;
            this.label = label;
            this.hint = hint;
            this.helperText = helperText;
            this.validationMessage = validationMessage;
            this.required = required;
            this.options = options;
        }

    }

    private static final FieldBinder SECTION_HEADER_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        header.setText(item.getLabel());
    };

    private static final FieldBinder TEXT_INPUT_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        EditText editText = itemView.findViewById(R.id.editText);
        if (item.hasError() && item.getValidationMessage() != null) {
            header.setText(item.getValidationMessage());
            header.setTextColor(Color.RED);
        } else if (item.isRequired()) {
            SpannableString spannableString = new SpannableString(item.getLabel() + " * ");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), item.getLabel().length() + 1, item.getLabel().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            header.setText(spannableString);
            header.setTextColor(Color.BLACK);
        } else {
            header.setText(item.getLabel());
            header.setTextColor(Color.BLACK);
        }
        editText.setBackgroundResource(item.isRequired() ? R.drawable.inputborder : R.drawable.normalinputborder);
        editText.setHint(item.getHint());

        TextWatcher textWatcher = (TextWatcher) editText.getTag();
        if (textWatcher != null) editText.removeTextChangedListener(textWatcher);
        editText.setText(item.getValue());

        TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                item.setValue(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        editText.addTextChangedListener(textWatcher1);
        editText.setTag(textWatcher1);
    };

    private static final FieldBinder DROP_DOWN_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        Spinner spinner = itemView.findViewById(R.id.dropdown);

        if (item.hasError() && item.getValidationMessage() != null) {
            header.setText(item.getValidationMessage());
            header.setTextColor(Color.RED);
        } else if (item.isRequired()) {
            SpannableString spannableString = new SpannableString(item.getLabel() + " * ");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), item.getLabel().length() + 1, item.getLabel().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            header.setText(spannableString);
            header.setTextColor(Color.BLACK);
        } else {
            header.setText(item.getLabel());
            header.setTextColor(Color.BLACK);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, item.getOptions());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(null);
        if(item.getValue() != null){
            int idx = item.getOptions().indexOf(item.getValue());
            if(idx >= 0) spinner.setSelection(idx);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setValue(item.getOptions().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    };

    private static final FieldBinder RADIO_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        TextView helper = itemView.findViewById(R.id.helperText);
        RadioGroup group = itemView.findViewById(R.id.radioGroup);
        RadioButton yes = itemView.findViewById(R.id.radioID1);
        RadioButton no = itemView.findViewById(R.id.radioID2);

        header.setText(item.getLabel());
        yes.setText(item.getOptions().get(0));
        no.setText(item.getOptions().get(1));
        helper.setVisibility(item.getHelperText() != null && !item.getHelperText().isEmpty() ? View.VISIBLE : View.GONE);
        helper.setText(item.getHelperText());

        group.setOnCheckedChangeListener(null);
        if("Yes".equals(item.getValue())) yes.setChecked(true);
        else if("No".equals(item.getValue())) no.setChecked(true);
        else group.clearCheck();
        group.setOnCheckedChangeListener((g, checkedId) -> item.setValue(checkedId == yes.getId() ? "Yes" : "No"));

    };

    private static final FieldBinder DATE_INPUT_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        EditText dateInput = itemView.findViewById(R.id.dateInput);

        if(item.hasError() && item.getValidationMessage() != null){
            header.setText(item.getValidationMessage());
            header.setTextColor(Color.RED);
        } else if (item.isRequired()) {
            SpannableString spannableString = new SpannableString(item.getLabel() + " * ");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), item.getLabel().length() + 1, item.getLabel().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            header.setText(spannableString);
            header.setTextColor(Color.BLACK);
        }
        else {
            header.setText(item.getLabel());
            header.setTextColor(Color.BLACK);
        }
        dateInput.setBackgroundResource(item.isRequired() ? R.drawable.inputborder : R.drawable.normalinputborder);
        dateInput.setHint(item.getHint());
        dateInput.setText(item.getValue());

        dateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    itemView.getContext(), (view, year, month, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month+1) + "/" + year;
                        dateInput.setText(date);
                        item.setValue(date);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });
    };

    private static final FieldBinder ATTACHMENT_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        ImageView imageView = itemView.findViewById(R.id.icon);

        if(item.hasError() && item.getValidationMessage() != null){
            header.setText(item.getValidationMessage());
            header.setTextColor(Color.RED);
        }
        else if(item.isRequired()){
            SpannableString spannableString = new SpannableString(item.getLabel() + " * ");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), item.getLabel().length() + 1, item.getLabel().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            header.setText(spannableString);
            header.setTextColor(Color.BLACK);
        }
        else {
            header.setText(item.getLabel());
            header.setTextColor(Color.BLACK);
        }

        imageView.setBackgroundResource(item.isRequired() ? R.drawable.inputborder : R.drawable.normalinputborder);
        imageView.setImageResource(android.R.drawable.ic_menu_upload);

        imageView.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("*/*");
            Intent chooser = Intent.createChooser(intent, "Select Image");
            Activity activity = (Activity) itemView.getContext();
            int requestCode = 1000 + item.getListIndex();
            activity.startActivityForResult(chooser, requestCode);
        });
    };

    private static final FieldBinder SEARCH_BOX_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        SearchView searchView = itemView.findViewById(R.id.searchBox);

        header.setText(item.getLabel());
        searchView.setQueryHint(item.getHint());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String newText) {
                item.setValue(newText);
                return true;
            }
        });
    };

    private FieldBinder bindButton(List<FormItem> items, Runnable onValidated) {
        return (itemView, item) -> {
            Button button = itemView.findViewById(R.id.visitButton);
            button.setText(item.getLabel());

            button.setOnClickListener(v -> {
                boolean isValid = true;
                for(FormItem item1 : items) {
                    if (item1.isRequired()) {
                        if (item1.getValue() == null || item1.getValue().isEmpty()) {
                            item1.setError(true);
                            isValid = false;
                        } else {
                            item1.setError(false);
                        }
                    }
                }
                onValidated.run();
                    if(isValid) {
                        Toast.makeText(itemView.getContext(), "Start Visit Clicked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(itemView.getContext(), "Please Fill the required details", Toast.LENGTH_SHORT).show();
                    }
            });
        };
    };

    public void computeGroupPosition(List<FormItem> items){
        int groupStart = -1;
        for(int i = 0; i< items.size(); i++){
            if("SECTION_HEADER".equals(items.get(i).getType())){
                if(groupStart != -1){
                    assignPosition(items, groupStart, i - 1);
                }
                else if(i > 0){
                    assignPosition(items, 0, i - 1);
                }
                groupStart=i;
            }
        }
        if(groupStart != -1) assignPosition(items, groupStart, items.size() - 1);
    }

    public void assignPosition(List<FormItem> items, int start, int end){
        if(start == end){
            items.get(start).setGroupPosition(FormItem.GroupPosition.SINGLE);
            return;
        }
        for(int i = start; i <= end; i++){
            if(i == start) items.get(i).setGroupPosition(FormItem.GroupPosition.TOP);
            else if(i == end) items.get(i).setGroupPosition(FormItem.GroupPosition.BOTTOM);
            else items.get(i).setGroupPosition(FormItem.GroupPosition.MIDDLE);
        }
    }

    public List<FormItem> buildFormItems(Runnable onValidated){
        List<FieldConfig> config = new ArrayList<>();

        //FollowUp visit
        config.add(new FieldConfig("SECTION_HEADER", "Follow Up Visit", null, null, null, false, null));
        config.add(new FieldConfig("RADIO_BUTTON", "Follow Up Visit", null, "(Please select \"Yes\" only if creating the visit for Follow up)",null,false, Arrays.asList("Yes", "No")));

        //Search box
        config.add(new FieldConfig("SECTION_HEADER","Patient Search", null, null, null, false, null));
        config.add(new FieldConfig("SEARCH_BOX","Search Visit History","Search by Patient name", null, null, false, null));

        //Category
        config.add(new FieldConfig("SECTION_HEADER","Category", null, null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN","Visit Type", "Select", null, "Please select the Visit type", true, Arrays.asList("New Visit","Follow Up", "Check up","Emergency","Consultation")));
        config.add(new FieldConfig("DROP_DOWN", "Purpose of Visit", "Select", null, "Please select the purpose of visit", true, Arrays.asList("New Visit", "Consultation", "Vaccination")));

        //Country/branch
        config.add(new FieldConfig("SECTION_HEADER","Country/Branch", null, null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN", "Country","United States", null, null, false, Arrays.asList("India", "China", "South Korea", "Australia", "United States")));
        config.add(new FieldConfig("DROP_DOWN", "Branch", "Select Branch", null, null,true, Arrays.asList("Hyderabad", "Chennai", "Karnataka", "LA", "Canada", "Andra Pradesh")));

        //Demographic
        config.add(new FieldConfig("SECTION_HEADER", "Demographic",null, null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "First Name", "First Name", null, "Please enter your first name", true, null));
        config.add(new FieldConfig("TEXT_INPUT","Last Name","Last Name", null, "Please enter your last name", true, null));
        config.add(new FieldConfig("ATTACHMENT", "Take a picture of Patient's ID ", null, null, "Please take or attach a picture of Patient's ID", true, null));
        config.add(new FieldConfig("TEXT_INPUT", "Social Security Number", "XXXXXXXXX", null, "Social security number is required", true, null ));
        config.add(new FieldConfig("TEXT_INPUT", "Phone Number", "Phone Number", null, "Phone number is required", true, null));
        config.add(new FieldConfig("DATE_INPUT", "Date of Birth", "Date of Birth", null, "Date of birth is required", true, null));
        config.add(new FieldConfig("DATE_INPUT", "Date of Injury", "Date of Injury", null, "Date of injury is required", true, null));
        config.add(new FieldConfig("TEXT_INPUT", "Claim Number", "Claim Number", null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Patient's Email", null, null, null,false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Additional Emails", "Additional Emails", null, null, false, null));

        //Address
        config.add(new FieldConfig("SECTION_HEADER", "Address", null, null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Address", "Address", null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Address 2", "Address 2", null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "City", "City", null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN", "State", "Select", null, "Select State", true, Arrays.asList("Andhra Pradesh", "Hyderabad", "Karnataka", "Tamil Nadu", "Maharashtra", "Kerala")));
        config.add(new FieldConfig("TEXT_INPUT", "Zip", "ZIP (5 or 9 digits)", null, "ZIP is required", true, null));

        //Result
        config.add(new FieldConfig("SECTION_HEADER", "Result", null, null, null, false, null));
        config.add(new FieldConfig("ATTACHMENT", "Previous Visit Scanned docs: ", null, null, null, false, null));

        //Remote Patient
        config.add(new FieldConfig("SECTION_HEADER", "Remote Patient", null, null, null, false, null));
        config.add(new FieldConfig("RADIO_BUTTON", "Remote Patient", null, "(Please select \"Yes\" only if patient is not in room and his/her mobile number is known)", null, false, Arrays.asList("Yes", "No")));

        //Language
        config.add(new FieldConfig("SECTION_HEADER", "Language", null, null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN", "Language", "English (United States)", null, null, false, Arrays.asList("English(UK)", "English(US)", "Telugu", "Tamil", "Malayalam", "Kannada", "Hindi")));

        //Button
        config.add(new FieldConfig("BUTTON", "Start New Visit", null, null, null, false, null));

        //Looping through all the field config with form item.
        List<FormItem> items = new ArrayList<>();
        for(FieldConfig config1: config){
            items.add(createItem(config1));
        }

        for(int i = 0; i < items.size(); i++){
            items.get(i).setListIndex(i);
        }

        computeGroupPosition(items);

        for(FormItem item: items){
            if("BUTTON".equals(item.getType())){
                item.setBinder(bindButton(items, onValidated));
            }
        }
        return items;
    }


    //With this method every fields sets into their own field.
    private FormItem createItem(FieldConfig config){
        FormItem item = new FormItem();
        item.setType(config.type);
        item.setLabel(config.label);
        item.setHint(config.hint);
        item.setHelperText(config.helperText);
        item.setValidationMessage(config.validationMessage);
        item.setRequired(config.required);
        item.setOptions(config.options);

        switch (config.type){
            case "RADIO_BUTTON":
                item.setLayoutResId(R.layout.row_radio_group);
                item.setBinder(RADIO_BINDER);
                break;
            case "SECTION_HEADER":
                item.setLayoutResId(R.layout.row_section_header);
                item.setBinder(SECTION_HEADER_BINDER);
                break;
            case "DROP_DOWN":
                item.setLayoutResId(R.layout.row_dropdown);
                item.setBinder(DROP_DOWN_BINDER);
                break;
            case "TEXT_INPUT":
                item.setLayoutResId(R.layout.row_text_input);
                item.setBinder(TEXT_INPUT_BINDER);
                break;
            case "DATE_INPUT":
                item.setLayoutResId(R.layout.row_date_input);
                item.setBinder(DATE_INPUT_BINDER);
                break;
            case "ATTACHMENT":
                item.setLayoutResId(R.layout.row_attachment);
                item.setBinder(ATTACHMENT_BINDER);
                break;
            case "SEARCH_BOX":
                item.setLayoutResId(R.layout.row_search_box);
                item.setBinder(SEARCH_BOX_BINDER);
                break;
            case "BUTTON":
                item.setLayoutResId(R.layout.row_button);
                break;
        }
        return item;
    }
}
