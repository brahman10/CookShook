package com.example.cookshook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.R;
import com.example.cookshook.RecipeActivity;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.MyViewHolder> {

    private List<RecipeModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name,title;
        LinearLayout suggest;
        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imgSuggest);
            name = view.findViewById(R.id.txtNameSugg);
            title = view.findViewById(R.id.txtTitleSugg);
            suggest=view.findViewById(R.id.llSuggest);
        }
    }

    public SuggestAdapter(List<RecipeModel> modelList , Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public SuggestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_suggest, parent, false);

        return new SuggestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuggestAdapter.MyViewHolder holder, int position) {
        RecipeModel mList = modelList.get(position);
        if (mList.getImgurl()!=null)
        {
            Glide.with(context).load(mList.getImgurl()).into(holder.img);
        }
        holder.name.setText(mList.getRecipename());
        holder.title.setText(mList.getCuisine());
        Glide.with(context).load(mList.getImgurl()).into(holder.img);
        holder.suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra("dishmodel",modelList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}


