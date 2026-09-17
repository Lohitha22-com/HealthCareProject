package com.example.healthcareproject.form;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.example.healthcareproject.R;
import com.example.healthcareproject.ui.patients.PatientsActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BuildFormItems {

    private static class FieldConfig{
        String type, label, hint, helperText, validationMessage, label2, hint2, validationMessage2;
        boolean required;
        boolean required2;
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

        FieldConfig(String type, String label, String label2, String hint, String hin2, String helperText, String validationMessage, String validationMessage2, boolean required,
                    boolean required2, List<String> options){
            this.type = type;
            this.label = label;
            this.label2 = label2;
            this.hint = hint;
            this.hint2 = hint2;
            this.helperText = helperText;
            this.validationMessage = validationMessage;
            this.validationMessage2 = validationMessage2;
            this.required = required;
            this.required2 = required2;
            this.options = options;
        }

    }

    private static ColorStateList createMaterialColorStateList(int stateColor){
        return new ColorStateList(new int[][]{
                new int[] {android.R.attr.state_focused},
                new int[] {}
        }, new int[] { stateColor, stateColor}
        );
    }

    private static final FieldBinder SECTION_HEADER_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        if(header != null){
            header.setText(item.getLabel());
            header.setBackgroundResource(R.drawable.section_header_background);
        }
    };

    private static final FieldBinder TEXT_INPUT_BINDER = (itemView, item) -> {
        TextInputLayout inputLayout = itemView.findViewById(R.id.textInputLayout);
        TextInputEditText editText = inputLayout.findViewById(R.id.editText);
        if(inputLayout != null){
            inputLayout.setHint(item.isRequired() ? item.getLabel() + " *" : item.getLabel());

            if(item.isRequired()){
                ColorStateList redColors = createMaterialColorStateList(Color.RED);
                inputLayout.setBoxStrokeColor(Color.RED);
                inputLayout.setBoxStrokeColorStateList(redColors);
                inputLayout.setHintTextColor(redColors);
            }
            else {
                int grayInt = Color.parseColor("#E0E0E0");
                ColorStateList grayColors = createMaterialColorStateList(grayInt);
                inputLayout.setBoxStrokeColor(grayInt);
                inputLayout.setBoxStrokeColorStateList(grayColors);
                inputLayout.setHintTextColor(grayColors);
            }

            if(item.hasError() && item.getValidationMessage() != null){
                inputLayout.setErrorEnabled(true);
                inputLayout.setError(item.getValidationMessage());
            }else {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }
        }

        if(inputLayout != null && editText != null){
            inputLayout.setEnabled(item.isEnabled());
            editText.setEnabled(item.isEnabled());
            inputLayout.setAlpha(item.isEnabled() ? 1.0f : 0.5f);
        }

        if(editText != null){
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
        }

    };

    FormItem itemInstance = new FormItem();


    private static final FieldBinder DROP_DOWN_BINDER = (itemView, item) -> {
        TextInputLayout inputLayout = itemView.findViewById(R.id.dropdownInputLayout);
        Spinner spinner = itemView.findViewById(R.id.dropdown);
        TextView labelTextView = itemView.findViewById(R.id.headerText);

        String labelText = item.isRequired() ? item.getLabel() + " *" : item.getLabel();

        if(labelText != null){
            labelTextView.setText((CharSequence) labelText);
            labelTextView.setVisibility(View.VISIBLE);
        }

        if(inputLayout != null){
            inputLayout.setHint(labelText);
            inputLayout.setBackgroundResource(R.drawable.text_border_color);

            if(item.isRequired()){
                ColorStateList redColors = createMaterialColorStateList(Color.RED);
                inputLayout.setBoxStrokeColor(Color.RED);
                inputLayout.setBoxStrokeColorStateList(redColors);
                inputLayout.setHintTextColor(redColors);
            }
            else {
                int grayInt = Color.parseColor("#808080");
                ColorStateList grayColors = createMaterialColorStateList(grayInt);
                inputLayout.setBoxStrokeColor(grayInt);
                inputLayout.setBoxStrokeColorStateList(grayColors);
                inputLayout.setHintTextColor(grayColors);
            }

            if(item.hasError() && item.getValidationMessage() != null){
                inputLayout.setErrorEnabled(true);
                inputLayout.setError(item.getValidationMessage());
            } else {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }
            inputLayout.setBoxBackgroundColor(Color.TRANSPARENT);
        }

        if(inputLayout != null && spinner != null){
            inputLayout.setEnabled(item.isEnabled());
            spinner.setEnabled(item.isEnabled());
            inputLayout.setAlpha(item.isEnabled() ? 1.0f : 0.5f);
        }

        if(spinner != null && item.getOptions() != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, item.getOptions()){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view).setTextColor(Color.BLACK);
                    return view;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    ((TextView) view).setTextColor(Color.BLACK);
                    return view;
                }
            };

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(null);
            spinner.setSelection(0, true);

            if(item.getValue() != null){
                int idx = item.getOptions().indexOf(item.getValue());
                if(idx >= 0) spinner.setSelection(idx);
            }

            final boolean[] isItemSelected = {false};
            spinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    isItemSelected[0] = true;
                    return false;
                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!isItemSelected[0]) return;

                    String selectedValue = adapterView.getItemAtPosition(i).toString();
                    item.setValue(selectedValue);

                    Log.e(item.getLabel().toString(),"LABEL 12345");

                    if(item.getLabel().equals("Country")){
                        boolean isUS = selectedValue.equals("United States");
                        Context context = itemView.getContext();
                        if(context instanceof PatientsActivity){
                            ((PatientsActivity) context).selectedCountry(isUS);
                        }
                    }

                    if(item.getLabel().toString().equals("Visit Type")&&i>0){
                        ArrayList<Integer> integerList = (ArrayList<Integer>) item.getOptions2();
                        if (integerList != null && i < integerList.size()) {
                            Context context = itemView.getContext();
                            if (context instanceof PatientsActivity) {
                                ((PatientsActivity) context).loadPurposeOfVisitData(integerList, i);
                            }
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
    };

    private static final FieldBinder RADIO_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        TextView helper = itemView.findViewById(R.id.helperText);
        RadioGroup group = itemView.findViewById(R.id.radioGroup);
        RadioButton yes = itemView.findViewById(R.id.radioID1);
        RadioButton no = itemView.findViewById(R.id.radioID2);

        if (header != null) header.setText(item.getLabel());
        if (yes != null && item.getOptions() != null && item.getOptions().size() > 0) yes.setText(item.getOptions().get(0));
        if (no != null && item.getOptions() != null && item.getOptions().size() > 1) no.setText(item.getOptions().get(1));

        if (helper != null) {
            helper.setVisibility(item.getHelperText() != null && !item.getHelperText().isEmpty() ? View.VISIBLE : View.GONE);
            helper.setText(item.getHelperText());
        }

        if (group != null && yes != null && no != null) {
            group.setOnCheckedChangeListener(null);
            if("Yes".equals(item.getValue())) yes.setChecked(true);
            else if("No".equals(item.getValue())) no.setChecked(true);
            else group.clearCheck();
            group.setOnCheckedChangeListener((g, checkedId) -> item.setValue(checkedId == yes.getId() ? "Yes" : "No"));
        }
    };

    private static final FieldBinder DOUBLE_DATE_INPUT_BINDER = (itemView, item) -> {
        TextInputLayout inputLayout1 = itemView.findViewById(R.id.dateInputLayout1);
        TextInputLayout inputLayout2 = itemView.findViewById(R.id.dateInputLayout2);
        EditText dateInput1 = itemView.findViewById(R.id.dateInput1);
        EditText dateInput2 = itemView.findViewById(R.id.dateInput2);

        if(inputLayout1 != null){
            inputLayout1.setHint(item.isRequired() ? item.getLabel() + " *" : item.getLabel());

            if(item.isRequired()){
                ColorStateList redColors = createMaterialColorStateList(Color.RED);
                inputLayout1.setBoxStrokeColor(Color.RED);
                inputLayout1.setBoxStrokeColorStateList(redColors);
                inputLayout1.setHintTextColor(redColors);
            } else {
                int grayInt = Color.parseColor("#808080");
                ColorStateList grayColors = createMaterialColorStateList(grayInt);
                inputLayout1.setBoxStrokeColor(grayInt);
                inputLayout1.setBoxStrokeColorStateList(grayColors);
                inputLayout1.setHintTextColor(grayColors);
            }

            if(item.hasError() && item.getValidationMessage() != null){
                inputLayout1.setErrorEnabled(true);
                inputLayout1.setError(item.getValidationMessage());
            } else {
                inputLayout1.setError(null);
                inputLayout1.setErrorEnabled(false);
            }
        }
        if(dateInput1 != null){
            dateInput1.setText(item.getValue());
            dateInput1.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(
                        itemView.getContext(), (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month+1) + "/" + year;
                    dateInput1.setText(date);
                    item.setValue(date);
                    if(inputLayout1 != null){
                        inputLayout1.setError(null);
                        inputLayout1.setErrorEnabled(false);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            });
        }
        if(inputLayout2 != null){
            inputLayout2.setHint(item.isRequired2() ? item.getLabel2() + " *" : item.getLabel2());

            if(item.isRequired2()){
                ColorStateList redColors = createMaterialColorStateList(Color.RED);
                inputLayout2.setBoxStrokeColor(Color.RED);
                inputLayout2.setBoxStrokeColorStateList(redColors);
                inputLayout2.setHintTextColor(redColors);
            } else {
                int grayInt = Color.parseColor("#808080");
                ColorStateList grayColors = createMaterialColorStateList(grayInt);
                inputLayout2.setBoxStrokeColor(grayInt);
                inputLayout2.setBoxStrokeColorStateList(grayColors);
                inputLayout2.setHintTextColor(grayColors);
            }

            if(item.hasError() && item.getValidationMessage2() != null){
                inputLayout2.setErrorEnabled(true);
                inputLayout2.setError(item.getValidationMessage2());
            } else {
                inputLayout2.setError(null);
                inputLayout2.setErrorEnabled(false);
            }
        }
        if(dateInput2 != null){
            dateInput2.setText(item.getValue2());
            dateInput2.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(
                        itemView.getContext(), (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month+1) + "/" + year;
                    dateInput2.setText(date);
                    item.setValue2(date);
                    if(inputLayout2 != null){
                        inputLayout2.setError(null);
                        inputLayout2.setErrorEnabled(false);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            });
        }
    };

    private static final FieldBinder PHONE_INPUT_BINDER = (itemView, item) -> {
        TextInputLayout inputLayout = itemView.findViewById(R.id.textInputLayout);
        TextInputEditText editText = itemView.findViewById(R.id.phoneEditText);
        Spinner spinner = itemView.findViewById(R.id.countryCodeSpinner);

        if(inputLayout != null){
            inputLayout.setHint(item.isRequired() ? item.getLabel() + " *" : item.getLabel());
            if(item.isRequired()){
                ColorStateList redColors = createMaterialColorStateList(Color.RED);
                inputLayout.setBoxStrokeColor(Color.RED);
                inputLayout.setBoxStrokeColorStateList(redColors);
                inputLayout.setHintTextColor(redColors);
            } else {
                int grayInt = Color.parseColor("#808080");
                ColorStateList grayColors = createMaterialColorStateList(grayInt);
                inputLayout.setBoxStrokeColor(grayInt);
                inputLayout.setBoxStrokeColorStateList(grayColors);
                inputLayout.setHintTextColor(grayColors);
            }

            if(item.hasError()){
                inputLayout.setBoxBackgroundColor(Color.parseColor("#FFCDD2"));
            } else {
                inputLayout.setBoxBackgroundColor(Color.TRANSPARENT);
            }
            inputLayout.setBoxBackgroundColor(Color.TRANSPARENT);
        }

        if(spinner != null && item.getOptions() != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, item.getOptions());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(null);
        }

        if(editText != null){
            TextWatcher textWatcher = (TextWatcher) editText.getTag();
            if (textWatcher != null) editText.removeTextChangedListener(textWatcher);
            editText.setText(item.getValue());

            TextWatcher textWatcher1 = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) { item.setValue(s.toString()); }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            };
            editText.addTextChangedListener(textWatcher1);
            editText.setTag(textWatcher1);
        }
    };

    private static final FieldBinder ATTACHMENT_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        ImageView imageView = itemView.findViewById(R.id.icon);

        if(header != null) {
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
        }

        if(item.getValue() != null){
            imageView.setImageURI(Uri.parse(item.getValue()));

            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                Intent chooser = Intent.createChooser(intent, "Select Image");
                Activity activity = (Activity) itemView.getContext();
                int requestCode = 1000 + item.getListIndex();
                activity.startActivityForResult(chooser, requestCode);
            });
        }
        else if (imageView != null) {
            imageView.setBackgroundResource(item.isRequired() ? R.drawable.inputborder : R.drawable.normalinputborder);
            imageView.setImageResource(android.R.drawable.ic_menu_upload);

            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                Intent chooser = Intent.createChooser(intent, "Select Image");
                Activity activity = (Activity) itemView.getContext();
                int requestCode = 1000 + item.getListIndex();
                activity.startActivityForResult(chooser, requestCode);
            });
        }
    };

    private static final FieldBinder SEARCH_BOX_BINDER = (itemView, item) -> {
        TextView header = itemView.findViewById(R.id.headerText);
        SearchView searchView = itemView.findViewById(R.id.searchBox);

        if(header!=null) header.setText(item.getLabel());
        if(searchView != null){
            searchView.setQueryHint(item.getHint());
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String newText) {
                    item.setValue(newText);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
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
        config.add(new FieldConfig("SECTION_HEADER","Category", null, null, null, true, null));
        config.add(new FieldConfig("DROP_DOWN","Visit Type", null, null, "Please select the Visit type", true, null));
        config.add(new FieldConfig("DROP_DOWN", "Purpose of Visit", null, null, "Please select the purpose of visit", true, null));

        //Country/branch
        config.add(new FieldConfig("SECTION_HEADER","Country/Branch", null, null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN", "Country",null, null, null, true, null));
        config.add(new FieldConfig("DROP_DOWN", "Branch", null, null, null,true, Arrays.asList("Select", "TEST TEST")));

        //Demographic
        config.add(new FieldConfig("SECTION_HEADER", "Demographic",null, null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "First Name", "First Name", null, "Please enter your first name", true, null));
        config.add(new FieldConfig("TEXT_INPUT","Last Name","Last Name", null, "Please enter your last name", true, null));
        config.add(new FieldConfig("ATTACHMENT", "Take a picture of Patient's ID ", null, null, "Please take or attach a picture of Patient's ID", true, null));
        config.add(new FieldConfig("TEXT_INPUT", "Social Security Number", "XXXXXXXXX", null, "Social security number is required", true, null ));
        config.add(new FieldConfig("PHONE_INPUT", "Phone Number", "Phone Number", null, "Phone number is required", true,null));

        //Date Input
        config.add(new FieldConfig("DOUBLE_DATE_INPUT", "Date of Birth","Date of Injury", "Date of Birth", "Date of Injury", null, "Date of Birth is required", "Date of Injurty required", true, true, null));

        config.add(new FieldConfig("TEXT_INPUT", "Claim Number", "Claim Number", null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Patient's Email", null, null, null,false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Additional Emails", "Additional Emails", null, null, false, null));

        //Address
        config.add(new FieldConfig("SECTION_HEADER", "Address", null, null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Address", "Address", null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "Address 2", "Address 2", null, null, false, null));
        config.add(new FieldConfig("TEXT_INPUT", "City", "City", null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN", "State", "Select", null, "Select State", true,null));
        config.add(new FieldConfig("TEXT_INPUT", "Zip", "ZIP (5 or 9 digits)", null, "ZIP is required", true, null));

        //Result
        config.add(new FieldConfig("SECTION_HEADER", "Result", null, null, null, false, null));
        config.add(new FieldConfig("ATTACHMENT", "Previous Visit Scanned docs: ", null, null, null, false, null));

        //Remote Patient
        config.add(new FieldConfig("SECTION_HEADER", "Remote Patient", null, null, null, false, null));
        config.add(new FieldConfig("RADIO_BUTTON", "Remote Patient", null, "(Please select \"Yes\" only if patient is not in room and his/her mobile number is known)", null, false, Arrays.asList("Yes", "No")));

        //Language
        config.add(new FieldConfig("SECTION_HEADER", "Language", null, null, null, false, null));
        config.add(new FieldConfig("DROP_DOWN", "Language", "English (United States)", null, null, false, null));

        //Looping through all the field config with form item.
        List<FormItem> items = new ArrayList<>();
        for(FieldConfig config1: config){
            items.add(createItem(config1));
        }

        for(int i = 0; i < items.size(); i++){
            items.get(i).setListIndex(i);
        }

        computeGroupPosition(items);

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
            case "PHONE_INPUT":
                item.setLayoutResId(R.layout.row_phone_input);
                item.setBinder(PHONE_INPUT_BINDER);
                break;
            case "TEXT_INPUT":
                item.setLayoutResId(R.layout.row_text_input);
                item.setBinder(TEXT_INPUT_BINDER);
                break;
            case "DOUBLE_DATE_INPUT":
                item.setLayoutResId(R.layout.row_double_date);
                item.setBinder(DOUBLE_DATE_INPUT_BINDER);
                item.setLabel2(config.label2);
                item.setHint2(config.hint2);
                item.setValidationMessage2(config.validationMessage2);
                item.setRequired2(config.required2);
                break;
            case "ATTACHMENT":
                item.setLayoutResId(R.layout.row_attachment);
                item.setBinder(ATTACHMENT_BINDER);
                break;
            case "SEARCH_BOX":
                item.setLayoutResId(R.layout.row_search_box);
                item.setBinder(SEARCH_BOX_BINDER);
                break;
        }
        return item;
    }
}
