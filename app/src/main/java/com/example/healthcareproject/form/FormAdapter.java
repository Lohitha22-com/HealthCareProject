package com.example.healthcareproject.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareproject.R;

import java.util.ArrayList;
import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    private List<FormItem> formItemsList;
    public FormAdapter(ArrayList<FormItem> formItemsList) {
     this.formItemsList = (formItemsList != null) ? formItemsList : new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return formItemsList.get(position).getLayoutResId();
    }

    public void setItems(List<FormItem> items){
        this.formItemsList = (items != null) ? items : new ArrayList<>();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FormAdapter.FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new FormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormAdapter.FormViewHolder holder, int position) {
        if(formItemsList == null || position < 0 || position >= formItemsList.size()) return;
        FormItem item = formItemsList.get(position);

        if(item.getBinder() != null) {
            item.getBinder().bind(holder.itemView, item);
        }
        applyingGroupPosition(holder.itemView, item);
    }

    public void applyingGroupPosition(View itemView, FormItem item){
        int bgBorder;
        switch (item.getGroupPosition()){
            case TOP: bgBorder = R.drawable.card_top; break;
            case BOTTOM: bgBorder = R.drawable.card_bottom; break;
            case SINGLE: bgBorder = R.drawable.card_single; break;
            default:bgBorder = R.drawable.card_middle; break;
        }
        itemView.setBackgroundResource(bgBorder);

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        int side = dpToPx(itemView.getContext(), 8);
        int overlap = 0;
        int top = (item.getGroupPosition() == FormItem.GroupPosition.TOP || item.getGroupPosition() == FormItem.GroupPosition.SINGLE) ? dpToPx(itemView.getContext(), 12) : overlap;
        int bottom = (item.getGroupPosition() == FormItem.GroupPosition.BOTTOM || item.getGroupPosition() == FormItem.GroupPosition.SINGLE) ? dpToPx(itemView.getContext(), 24) : 0;
        marginLayoutParams.setMargins(side, top, side, bottom);
        itemView.setLayoutParams(marginLayoutParams);
    }

    public int dpToPx(Context context, int dp){
        int i = (int) (dp * context.getResources().getDisplayMetrics().density);
        return i;
    }


    @Override
    public int getItemCount() {
        return formItemsList != null ? formItemsList.size() : 0;
    }

    public static class FormViewHolder extends RecyclerView.ViewHolder {
        public FormViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
