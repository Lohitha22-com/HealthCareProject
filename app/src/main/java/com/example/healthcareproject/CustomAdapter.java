//package com.example.healthcareproject;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import androidx.annotation.NonNull;
//
//import java.text.BreakIterator;
//import java.util.List;
//import java.util.Map;
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
//
//    private Context context;
//
//    private List<RecycleViewDashboard> items;
//
//
//    public CustomAdapter(Context context, List<RecycleViewDashboard> items){
//        this.context = context;
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType )
//    {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitemdash,parent, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
//       RecycleViewDashboard item = items.get(position);
//       holder.textView1.setText("Name: " + item.getFirstName() + " " +item.getLastName());
//       holder.deleteButton.setOnClickListener(view -> {
//           int currentPosition = holder.getAdapterPosition();
//           if(currentPosition != RecyclerView.NO_POSITION){
//               items.remove(currentPosition);
//               notifyItemRemoved(currentPosition);
//           }
//       });
//       holder.textVisit.setText("Visit_Date: " + item.getVisit_Completed());
//       holder.textCompany.setText("Company_name: " + item.getCompany_name());
//       holder.textFollowUpCount.setText(String.valueOf("Followup_Count: "+item.getFollowUpCount()));
//       holder.textLanguage.setText("Language_Name: " + item.getLanguageName());
//       if(item.getFollowUpCount() > 0) {
//           holder.itemRoot.setCardBackgroundColor(Color.RED);
//           holder.textView1.setTextColor(Color.WHITE);
//           holder.textVisit.setTextColor(Color.WHITE);
//           holder.textCompany.setTextColor(Color.WHITE);
//           holder.textFollowUpCount.setTextColor(Color.WHITE);
//           holder.textLanguage.setTextColor(Color.WHITE);
//       }
//       else {
//           holder.itemRoot.setCardBackgroundColor(Color.WHITE);
//           holder.textView1.setTextColor(Color.BLUE);
//           holder.textVisit.setTextColor(Color.BLUE);
//           holder.textCompany.setTextColor(Color.BLUE);
//           holder.textFollowUpCount.setTextColor(Color.BLUE);
//           holder.textLanguage.setTextColor(Color.BLUE);
//       }
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        public CardView itemRoot;
//        TextView textView1;
//        TextView textVisit;
//        TextView textFollowUpCount;
//        TextView textCompany;
//        ImageButton deleteButton;
//        TextView textLanguage;
//        public MyViewHolder(@NonNull View itemView){
//            super(itemView);
//
//            textView1 = itemView.findViewById(R.id.text2);
//            textVisit = itemView.findViewById(R.id.textVisit);
//            textFollowUpCount = itemView.findViewById(R.id.textFollowUpCount);
//            itemRoot = itemView.findViewById(R.id.itemRoot);
//            textCompany = itemView.findViewById(R.id.textCompany);
//            deleteButton = itemView.findViewById(R.id.deleteButton);
//            textLanguage = itemView.findViewById(R.id.textLanguage);
//        }
//    }
//
//}
