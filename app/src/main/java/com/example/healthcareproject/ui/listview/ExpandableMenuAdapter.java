package com.example.healthcareproject.ui.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.healthcareproject.R;
import com.example.healthcareproject.data.model.MenuModelClass;

import java.util.ArrayList;
import java.util.List;

public class ExpandableMenuAdapter extends BaseExpandableListAdapter {

    private List<MenuModelClass> topLevelItems = new ArrayList<>();


    public ExpandableMenuAdapter(List<MenuModelClass> topLevelItems){
        this.topLevelItems = topLevelItems;
    }

    @Override
    public int getGroupCount() {
        return topLevelItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return topLevelItems.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return topLevelItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return topLevelItems.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return topLevelItems.get(groupPosition).getMenu_id();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return topLevelItems.get(groupPosition).getChildren().get(childPosition).getMenu_id();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_row, parent, false);
        }
        MenuModelClass group = topLevelItems.get(groupPosition);

        TextView titleText = convertView.findViewById(R.id.groupTitle);
        titleText.setText(group.getTitle());

        TextView arrowText = convertView.findViewById(R.id.groupArrow);

        if(group.getChildren().size() > 0){
            arrowText.setVisibility(View.VISIBLE);
            arrowText.setText(isExpanded ? "˄" : "⌄");
            arrowText.setTextSize(16);
        }
        else {
            arrowText.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item_row, parent, false);
        }

        MenuModelClass child = topLevelItems.get(groupPosition).getChildren().get(childPosition);

        TextView titleText = convertView.findViewById(R.id.childTitle);
        titleText.setText(child.getTitle());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
