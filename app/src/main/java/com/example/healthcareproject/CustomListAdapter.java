package com.example.healthcareproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import java.util.List;
import java.util.Map;

public class CustomListAdapter extends BaseAdapter {

    private List<ListViewDashboard> items;
    private Context context;

    LayoutInflater inflater;

    public CustomListAdapter(Context context, List<ListViewDashboard> items){
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SpannableString getStyleTexts(String label, String value, int labelColor){
        String paddedLabel = String.format("%-18s", label);
        String text = paddedLabel + " : \t" + value;
        SpannableString spannableString = new SpannableString(text);
        int indexColon = text.toString().indexOf(" : ");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0 , indexColon + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, indexColon + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TypefaceSpan("monospace"), 0, indexColon + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemslistdashboard, parent, false);
        }
        ListViewDashboard item = items.get(position);

        CardView cardView = convertView.findViewById(R.id.itemRoot);
        LinearLayout dynamicContainer = convertView.findViewById(R.id.dynamicContainer);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        ImageButton timerButton = convertView.findViewById(R.id.timerButton);

//        TextView nameText = convertView.findViewById(R.id.textName);
//        TextView visitText = convertView.findViewById(R.id.textVisit);
//        TextView visitIdText = convertView.findViewById(R.id.textVisitId);
//        TextView dateText = convertView.findViewById(R.id.textDate);
//        TextView timeText = convertView.findViewById(R.id.textTime);
//        TextView roomText = convertView.findViewById(R.id.textRoom);
//        TextView companyText = convertView.findViewById(R.id.textCompany);
//        TextView languageText = convertView.findViewById(R.id.textLanguage);
//        TextView purposeText = convertView.findViewById(R.id.textPurpose);
//        TextView followupText = convertView.findViewById(R.id.textFollowUpCount);
//        TextView visitDoneText = convertView.findViewById(R.id.textVisitDone);
//        TextView remotePatientText = convertView.findViewById(R.id.textRemotePatient);


//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (Map.Entry<String, String> entry : item.getItem().entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            stringBuilder.append(key)
//                    .append(" : ")
//                    .append(value)
//                    .append("\n");
//        }

//        nameText.setText(stringBuilder.toString());

//        nameText.setText(getStyleTexts("Patient_name", item.getFirstName() + " " + item.getLastName(), labelColor));
//        visitText.setText(getStyleTexts("Visit_Completed", item.getVisitCompleted(), labelColor));
//        companyText.setText(getStyleTexts("Company_Name", item.getCompanyName(), labelColor));
//        languageText.setText(getStyleTexts("Language_Name", item.getLanguageName(),labelColor));
//        followupText.setText(getStyleTexts("Followup_Count", String.valueOf(item.getFollowUpCount()), labelColor));

//
//        String visitId = item.getValue("visit_id");
//        String dateofService = item.getValue("date_of_service");
//        String timeIn = item.getValue("time_in");
//        String roomName = item.getValue("room_name");
//        String purposeOfVisit = item.getValue("PurposeofVisit");
//        String visitDone = item.getValue("visit_done");
//        String remotePatient = item.getValue("RemotePatient_YN");

//        visitIdText.setText( getStyleTexts("Visit_ID", safeValue(visitId), Color.BLACK) );
//        dateText.setText( getStyleTexts("Date", safeValue(dateofService), Color.BLACK) );
//        timeText.setText( getStyleTexts("Time_In", safeValue(timeIn), Color.BLACK) );
//        roomText.setText( getStyleTexts("Room", safeValue(roomName), Color.BLACK) );
//        purposeText.setText( getStyleTexts("Purpose", safeValue(purposeOfVisit), Color.BLACK) );
//        visitDoneText.setText( getStyleTexts("Visit_Done", safeValue(visitDone), Color.BLACK) );
//        remotePatientText.setText( getStyleTexts( "Remote_Patient", safeValue(remotePatient), Color.BLACK ) );

//        if(item.getFollowUpCount() > 0){
//            cardView.setCardBackgroundColor(Color.RED);
//            nameText.setTextColor(Color.WHITE);
//            visitText.setTextColor(Color.WHITE);
//            visitIdText.setTextColor(Color.WHITE);
//            dateText.setTextColor(Color.WHITE);
//            timeText.setTextColor(Color.WHITE);
//            roomText.setTextColor(Color.WHITE);
//            companyText.setTextColor(Color.WHITE);
//            languageText.setTextColor(Color.WHITE);
//            purposeText.setTextColor(Color.WHITE);
//            followupText.setTextColor(Color.WHITE);
//            visitDoneText.setTextColor(Color.WHITE);
//            remotePatientText.setTextColor(Color.WHITE);
//        }
//        else{
//            cardView.setCardBackgroundColor(Color.WHITE);
//            nameText.setTextColor(Color.BLUE);
//            visitText.setTextColor(Color.BLUE);
//            visitIdText.setTextColor(Color.BLUE);
//            dateText.setTextColor(Color.BLUE);
//            timeText.setTextColor(Color.BLUE);
//            roomText.setTextColor(Color.BLUE);
//            companyText.setTextColor(Color.BLUE);
//            languageText.setTextColor(Color.BLUE);
//            purposeText.setTextColor(Color.BLUE);
//            followupText.setTextColor(Color.BLUE);
//            visitDoneText.setTextColor(Color.BLUE);
//            remotePatientText.setTextColor(Color.BLUE);
//        }

        if (dynamicContainer != null) {
            dynamicContainer.removeAllViews();
            int labelColor = (item.getFollowUpCount() > 0) ? Color.WHITE : Color.BLACK;
            int textColor = (item.getFollowUpCount() > 0) ? Color.WHITE : Color.BLUE;

            Map<String, String> dataMap = item.getItem();
            if (dataMap != null && !dataMap.isEmpty()) {

                String firstName = safeValue(dataMap.get("first_name"));
                String lastName = safeValue(dataMap.get("last_name"));
                String fullName = (firstName + " " + lastName).trim();

                if(fullName.isEmpty()){
                    fullName = "null";
                }

                TextView nameTextView = new TextView(context);
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                nameParams.setMargins(0,4,0,4);
                nameTextView.setLayoutParams(nameParams);

                nameTextView.setText(getStyleTexts("Patient Name", fullName, labelColor));
                nameTextView.setTextColor(textColor);
                nameTextView.setTextSize(15);
                dynamicContainer.addView(nameTextView);

                String[] allowedKeys = {"Followup_Count", "date_of_service", "email_address"};

                for (String targetKey : allowedKeys) {
                    String currentValue = "";
                    if (dataMap.containsKey(targetKey)) {
                        currentValue = safeValue(dataMap.get(targetKey));
                    }

                    if (currentValue.trim().isEmpty()) {
                        currentValue = "null";
                    }

                    String displayLabel = formatLabelForUI(targetKey);

                    TextView textViewPair = new TextView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 4, 0, 4);
                    textViewPair.setLayoutParams(params);

                    textViewPair.setText(getStyleTexts(displayLabel, currentValue, labelColor));
                    textViewPair.setTextColor(textColor);
                    textViewPair.setTextSize(15);

                    dynamicContainer.addView(textViewPair);
                }
            }

            if (cardView != null) {
                if (item.getFollowUpCount() > 0) {
                    cardView.setCardBackgroundColor(Color.RED);
                } else {
                    cardView.setCardBackgroundColor(Color.WHITE);
                }
            }

            if(timerButton != null){
                timerButton.setOnClickListener(v -> {
                    if(item.getItem() != null) {
                        String firstName = safeValue(item.getItem().get("first_name"));
                        String lastName = safeValue(item.getItem().get("last_name"));
                        String fullName = (firstName + " " + lastName).trim();
                        if(fullName.isEmpty() || fullName.equalsIgnoreCase("Create a new visit")){
                            fullName = "Patient Profile";
                        }
                        String email = safeValue(item.getItem().get("email_address"));
                        String date = safeValue(item.getItem().get("date_of_service"));
                        String followUp = String.valueOf(item.getFollowUpCount());

                        if(date.trim().isEmpty()) date = "null";
                        if(email.trim().isEmpty()) email = "null";
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setTitle("\uD83D\uDCCB Visit Details");

                        String messageDetails = "Patient Name : " + fullName + "\n\n"
                                + "Visit Completed Date : " + date + "\n"
                                + "Email Address : " + email + "\n"
                                + "Follow-ups : " + followUp + " scheduled";

                        builder.setMessage(messageDetails);

                        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }

            if(deleteButton != null) {
                deleteButton.setOnClickListener(v -> {
                    items.remove(position);
                    notifyDataSetChanged();
                });
            }
        }
        return convertView;
    }
    String safeValue(String value) {
        return value == null ? "" : value;
    }
    private String formatLabelForUI(String key){
            if(key.equalsIgnoreCase("Followup_Count")) return "Followup Count";
            if(key.equalsIgnoreCase("date_of_service")) return "Visit Date";
            if(key.equalsIgnoreCase("email_address")) return "Email Address";
            return  key;
        }
}
