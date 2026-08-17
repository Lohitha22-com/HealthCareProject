//package com.example.healthcareproject;
//
//
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class RAdapter extends RecyclerView.Adapter<RAdapter.MyViewHolder> {
//
//    ArrayList<String> mobileNames;
//
//    Context context;
//
//
//    public RAdapter(Context context, ArrayList<String> mobileNames){
//        this.context = context;
//        this.mobileNames = mobileNames;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_of_recyclar_viewlist, parent, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.name.setText(mobileNames.get(position));
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, mobileNames.get(position).toString(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mobileNames.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView name;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.name);
//        }
//    }
//}
