package com.example.hackathon_covid19;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.hackathon_covid19.ViewHolder.decodeFromFirebaseBase64;


public class Essentials_issue_Adapter extends RecyclerView.Adapter<Essentials_issue_Adapter.ViewHolder> {
    List<Essen_issuePOJO> list = Collections.emptyList();
    Bitmap getimage=null;
    Context context;

    public Essentials_issue_Adapter(ArrayList<Essen_issuePOJO> issuePOJOS, Context context) {

        this.list = issuePOJOS;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.essen_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String img=list.get(position).getImage();

        holder.id.setText(list.get(position).getUserEmailId());
        String msg=list.get(position).getUserMessage();
        holder.userMessage.setText("Message :"+"\n"+msg);
        String data=list.get(position).getUserName();
        holder.postedBy.setText(data);
        holder.post_time.setText(list.get(position).getTime());


    }



    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // EditText name;
        TextView id,userMessage,postedBy,post_time;
        //TextView uploader;
        // TextView time;
        ViewHolder(View itemView) {
            super(itemView);
            // name = itemView.findViewById(R.id.edit_store_name);
            id=itemView.findViewById(R.id.id);
            userMessage= itemView.findViewById(R.id.detail_content);
            userMessage.setMovementMethod(new ScrollingMovementMethod());
            postedBy=itemView.findViewById(R.id.posted_by);
            post_time=itemView.findViewById(R.id.post_time);
           // storeName=itemView.findViewById(R.id.edit_store_name);

        }



    }
}

