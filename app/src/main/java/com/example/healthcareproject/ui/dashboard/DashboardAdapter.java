package com.example.healthcareproject.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcareproject.R;

import java.util.List;

public class DashboardAdapter extends ArrayAdapter<DashboardItem> {


    private Context context;
    private List<DashboardItem> items;

    LayoutInflater inflater;

    public DashboardAdapter(Context context, List<DashboardItem> items){
        super(context, R.layout.dashboardviewindex, items);
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.dashboardviewindex, parent, false);
        }
        TextView text = convertView.findViewById(R.id.list_item);
        ImageView image = convertView.findViewById(R.id.image_icon);

        DashboardItem item = items.get(position);

        text.setText(item.getTitle());
        image.setImageResource(item.getImage());

        return convertView;
    }
}
