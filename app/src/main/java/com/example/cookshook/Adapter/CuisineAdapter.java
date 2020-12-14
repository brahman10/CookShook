package com.example.cookshook.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.cookshook.DishlistActivity;
import com.example.cookshook.LoginActivity;
import com.example.cookshook.R;
import com.example.cookshook.Utils.CuisineLikeHandler;
import com.example.cookshook.Utils.SessionManagment;

import java.util.HashMap;
import java.util.List;

import static com.example.cookshook.Utils.Constants.KEY_ID;
import static com.example.cookshook.Utils.CuisineLikeHandler.COLUMN_ID;
import static com.example.cookshook.Utils.CuisineLikeHandler.COLUMN_IMG;
import static com.example.cookshook.Utils.CuisineLikeHandler.COLUMN_USER_ID;


public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.MyViewHolder> {

    private List<String> cuisineName;
    private List<String> cuisineImg;
    private Context context;
    CuisineLikeHandler cuisineLikeHandler;
    SessionManagment sessionManagment;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView room;
        TextView name;
        ImageView imageView,wish_before , wish_after;


        public MyViewHolder(View view) {
            super(view);
            room = view.findViewById(R.id.cardCuisine);
            name = view.findViewById(R.id.txtCuisine);
            imageView = view.findViewById(R.id.imgCuisine);
            wish_after = view.findViewById(R.id.wish_after);
            wish_before = view.findViewById(R.id.wish_before);
        }
    }

    public CuisineAdapter(List<String> cuisineImg , List<String> cuisineName , Context context) {
        this.cuisineImg = cuisineImg;
        this.cuisineName = cuisineName;
        this.context = context;
        this.cuisineLikeHandler = new CuisineLikeHandler(context);
        this.sessionManagment = new SessionManagment(context);

    }

    @Override
    public CuisineAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cuisine, parent, false);

        return new CuisineAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CuisineAdapter.MyViewHolder holder, int position) {
        holder.name.setText(cuisineName.get(position));
        Glide.with(context).load(cuisineImg.get(position)).into(holder.imageView);
        if (sessionManagment.isLogin())
        {
            if (cuisineLikeHandler.isInLiketable(cuisineName.get(position),sessionManagment.getUserDetails().get(KEY_ID)))
            {
                holder.wish_before.setVisibility(View.GONE);
                holder.wish_after.setVisibility(View.VISIBLE);
            }
        }

        holder.room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DishlistActivity.class);
                intent.putExtra("cuisine",cuisineName.get(position));
                context.startActivity(intent);
            }
        });

        holder.wish_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagment.isLogin())
                {
                    holder.wish_after.setVisibility(View.VISIBLE);
                    holder.wish_before.setVisibility(View.GONE);
                    HashMap<String,String> map = new HashMap<>();
                    map.put(COLUMN_ID,cuisineName.get(position));
                    map.put(COLUMN_IMG,cuisineImg.get(position));
                    map.put(COLUMN_USER_ID,sessionManagment.getUserDetails().get(KEY_ID));
                    cuisineLikeHandler.setwishTable(map);
                }
                else
                {
                    context.startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
                }
            }
        });
        holder.wish_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wish_after.setVisibility(View.GONE);
                holder.wish_before.setVisibility(View.VISIBLE);
                cuisineLikeHandler.removeItemFromWishtable(cuisineName.get(position),sessionManagment.getUserDetails().get(KEY_ID));
            }
        });


    }

    @Override
    public int getItemCount() {
        return cuisineName.size();
    }

}



