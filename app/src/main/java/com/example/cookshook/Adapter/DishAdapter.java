package com.example.cookshook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.R;
import com.example.cookshook.RecipeActivity;
import com.example.cookshook.Utils.RecipeLikeHandler;
import com.example.cookshook.Utils.SessionManagment;

import java.util.HashMap;
import java.util.List;

import static com.example.cookshook.Utils.Constants.KEY_ID;
import static com.example.cookshook.Utils.RecipeLikeHandler.COLUMN_ID;
import static com.example.cookshook.Utils.RecipeLikeHandler.COLUMN_USER_ID;


public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {

    private List<RecipeModel> dishList;
    private Context context;
    RecipeLikeHandler recipeLikeHandler;
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

    public DishAdapter(List<RecipeModel> dishList , Context context) {
        this.dishList = dishList;
        this.context = context;
        this.recipeLikeHandler = new RecipeLikeHandler(context);
        this.sessionManagment = new SessionManagment(context);
    }

    @Override
    public DishAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cuisine, parent, false);

        return new DishAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DishAdapter.MyViewHolder holder, int position) {
        holder.name.setText(dishList.get(position).getRecipename());
        Glide.with(context).load(dishList.get(position).getImgurl()).into(holder.imageView);
        if (sessionManagment.isLogin())
        {
            if (recipeLikeHandler.isInLiketable(dishList.get(position).getId(),sessionManagment.getUserDetails().get(KEY_ID)))
            {
                holder.wish_before.setVisibility(View.GONE);
                holder.wish_after.setVisibility(View.VISIBLE);
            }
        }
        holder.room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra("dishmodel",dishList.get(position));
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
                    map.put(COLUMN_ID,dishList.get(position).getId());
                    map.put(COLUMN_USER_ID,sessionManagment.getUserDetails().get(KEY_ID));
                    Log.e("map",map.toString());
                    recipeLikeHandler.setwishTable(map);
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
                recipeLikeHandler.removeItemFromWishtable(dishList.get(position).getId(),sessionManagment.getUserDetails().get(KEY_ID));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

}



