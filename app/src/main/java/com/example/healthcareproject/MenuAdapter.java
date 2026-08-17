//package com.example.healthcareproject;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
//
//    private final List<MenuModelClass> modelClassList = new ArrayList<>();
//
//    public interface OnMenuItemClickListener {
//        void onMenuItemClick(MenuModelClass item);
//    }
//
//    private OnMenuItemClickListener listener;
//
//    public MenuAdapter(List<MenuModelClass> topLevelItems, OnMenuItemClickListener listener){
//        modelClassList.addAll(topLevelItems);
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_row, parent, false);
//        return new MenuViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
//        MenuModelClass item = modelClassList.get(position);
//        holder.titleText.setText(item.getTitle());
//
//        if(item.getParent_id() != null){
//            holder.titleText.setPadding(60, 15, 15, 15);
//        }
//        else {
//            holder.titleText.setPadding(15, 15, 15, 15);
//        }
//
//        if("collapsible".equals(item.getType())){
//            holder.arrowText.setVisibility(View.VISIBLE);
//            holder.arrowText.setText(item.isExpanded() ? "▲" : "▼");
//        }
//        else{
//            holder.arrowText.setVisibility(View.INVISIBLE);
//        }
//
//        holder.itemView.setOnClickListener(v -> {
//            int currentPosition = holder.getAdapterPosition();
//            if(currentPosition == RecyclerView.NO_POSITION) return;
//
//            if("collapsible".equals(item.getType())) {
//                if (item.isExpanded()) {
//                    int childCount = item.getChildren().size();
//                    for (int i = 0; i < childCount; i++) {
//                        modelClassList.remove(currentPosition + 1);
//                    }
//                    item.setExpanded(false);
//                    notifyItemRangeRemoved(currentPosition + 1, childCount);
//                } else {
//                    modelClassList.addAll(currentPosition + 1, item.getChildren());
//                    item.setExpanded(true);
//                    notifyItemRangeInserted(currentPosition + 1, item.getChildren().size());
//                }
//            }
//                else if("item".equals(item.getType())){
//                    if(listener != null){
//                        listener.onMenuItemClick(item);
//                    }
//                }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return modelClassList.size();
//    }
//
//    public static class MenuViewHolder extends RecyclerView.ViewHolder{
//        TextView titleText;
//        TextView arrowText;
//
//        public MenuViewHolder(@NonNull View itemView){
//            super(itemView);
//            titleText = (TextView) itemView.findViewById(R.id.menuItem);
//            arrowText = (TextView) itemView.findViewById(R.id.menuArrow);
//        }
//    }
//}
