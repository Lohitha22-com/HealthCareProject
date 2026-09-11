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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<ListViewDashboard> items;

    public CustomRecyclerViewAdapter(Context context, List<ListViewDashboard> items) {
        this.context = context;
        this.items = items;
    }

    public SpannableString getStyleTexts(String label, String value, int labelColor) {
        String paddedLabel = String.format("%-18s", label);
        String text = paddedLabel + " : \t" + value;
        SpannableString spannableString = new SpannableString(text);
        int indexColon = text.toString().indexOf(" : ");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, indexColon + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, indexColon + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TypefaceSpan("monospace"), 0, indexColon + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemslistdashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListViewDashboard item = items.get(position);

        CardView cardView = holder.cardView;
        LinearLayout dynamicContainer = holder.dynamicContainer;
        ImageButton deleteButton = holder.deleteButton;
        ImageButton timerButton = holder.timerButton;

        if (dynamicContainer != null) {
            dynamicContainer.removeAllViews();
            int labelColor = (item.getFollowUpCount() > 0) ? Color.RED : Color.BLACK;
            int textColor = (item.getFollowUpCount() > 0) ? Color.RED : Color.BLUE;

            Map<String, String> dataMap = item.getItem();
            if (dataMap != null && !dataMap.isEmpty()) {

                String fullName = safeValue(dataMap.get("Patient_Name"));
                if(fullName.isEmpty()){
                    fullName = safeValue(dataMap.get("Patient_Name"));
                }
                if(fullName.isEmpty()) {
                    String firstName = safeValue(dataMap.get("first_name"));
                    String lastName = safeValue(dataMap.get("last_name"));
                    fullName = (firstName + " " + lastName).trim();
                }

                if (fullName.isEmpty()) {
                    fullName = "Unknown Patient";
                }

                TextView nameTextView = new TextView(context);
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                nameParams.setMargins(0, 4, 0, 4);
                nameTextView.setLayoutParams(nameParams);

                nameTextView.setText(getStyleTexts("Patient Name", fullName, labelColor));
                nameTextView.setTextColor(textColor);
                nameTextView.setTextSize(15);
                dynamicContainer.addView(nameTextView);

                String[] allowedKeys = {"Followup_Count", "date_of_service"};

                for (String targetKey : allowedKeys) {
                    String currentValue = "";

                    if (dataMap.containsKey(targetKey)) {
                        currentValue = safeValue(dataMap.get(targetKey));
                    } else if (targetKey.equals("date_of_service") && dataMap.containsKey("visit_date")) {
                        currentValue = safeValue(dataMap.get("visit_date"));
                    }

                    if (currentValue.trim().isEmpty() ||currentValue.equalsIgnoreCase("null")) {
                        currentValue = "Not Specified";
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

//            if (cardView != null) {
//                if (item.getFollowUpCount() > 0) {
//                    cardView.setCardBackgroundColor(Color.RED);
//                } else {
//                    cardView.setCardBackgroundColor(Color.WHITE);
//                }
//            }

            if (timerButton != null) {
                timerButton.setOnClickListener(v -> {
                    if (item.getItem() != null) {
                        Map<String, String> map = item.getItem();
                        String fullName = safeValue(map.get("Patient_Name"));
                        if (fullName.isEmpty()) fullName = safeValue(map.get("Patient_Name"));
                        if(fullName.isEmpty()){
                            String fName = safeValue(map.get("first_name"));
                            String lName = safeValue(map.get("last_name"));
                            fullName = (fName + " " + lName).trim();
                        }

                        if (fullName.isEmpty()) fullName = "Patient Profile";


                        String date = safeValue(map.get("date_of_service"));
                        if (date.isEmpty()) date = safeValue(map.get("visit_date"));

                        String followUpCountStr = String.valueOf(item.getFollowUpCount());

                        if (date.trim().isEmpty() || date.equalsIgnoreCase("null")) date = "Not Specified";

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("\uD83D\uDCCB Visit Details");

                        String messageDetails = "Patient Name : " + fullName + "\n\n"
                                + "Visit Completed Date : " + date + "\n"
                                + "Follow-ups : " + followUpCountStr + " scheduled";

                        builder.setMessage(messageDetails);

                        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
                        builder.create().show();
                    }
                });
            }

            if (deleteButton != null) {
                deleteButton.setOnClickListener(v -> {
                    int adapterPosition = holder.getBindingAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        items.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                    }
                });
            }
        }
    }

    String safeValue(String value) {
        return value == null ? "" : value;
    }

    private String formatLabelForUI(String key) {
        if (key.equalsIgnoreCase("Followup_Count")) return "Followup Count";
        if (key.equalsIgnoreCase("date_of_service")) return "Visit Date";
        return key;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        LinearLayout dynamicContainer;
        ImageButton deleteButton;
        ImageButton timerButton;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            cardView = itemView.findViewById(R.id.itemRoot);

            dynamicContainer = itemView.findViewById(R.id.dynamicContainer);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            timerButton = itemView.findViewById(R.id.timerButton);
        }
    }
}