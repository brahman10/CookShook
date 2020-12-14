package com.example.cookshook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cookshook.Adapter.CuisineAdapter;
import com.example.cookshook.Utils.CuisineLikeHandler;
import com.example.cookshook.Utils.LoadingBar;
import com.example.cookshook.Utils.SessionManagment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.cookshook.Utils.Constants.KEY_ID;
import static com.example.cookshook.Utils.Constants.KEY_MOBILE;
import static com.example.cookshook.Utils.CuisineLikeHandler.COLUMN_ID;
import static com.example.cookshook.Utils.CuisineLikeHandler.COLUMN_IMG;

public class FavouriteCuisine extends AppCompatActivity {

    CuisineLikeHandler cuisineLikeHandler;
    SessionManagment sessionManagment;
    ArrayList<HashMap<String,String>> likedcuisine;
    List<String> cuisineName;
    List<String> cuisineImage;
    RecyclerView recyclerLiked;
    CuisineAdapter cuisineAdapter;
    LoadingBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_cuisine);
        loadingBar = new LoadingBar(this);
        loadingBar.show();
        cuisineLikeHandler = new CuisineLikeHandler(this);
        sessionManagment = new SessionManagment(this);
        likedcuisine = new ArrayList<>();
        cuisineName = new ArrayList<>();
        cuisineImage = new ArrayList<>();
        recyclerLiked = findViewById(R.id.rec_like);
        Log.e("id",sessionManagment.getUserDetails().get(KEY_ID));
        likedcuisine = cuisineLikeHandler.getCuisineAll(sessionManagment.getUserDetails().get(KEY_ID));
        Log.e("  d",likedcuisine.toString());
        for (HashMap<String,String > hashMap:likedcuisine) {
            Log.e( "onCreate: ",hashMap.toString() );
            cuisineImage.add(hashMap.get(COLUMN_IMG));
            cuisineName.add(hashMap.get(COLUMN_ID));
        }
        cuisineAdapter = new CuisineAdapter(cuisineImage,cuisineName,this);
        recyclerLiked.setLayoutManager(new GridLayoutManager(this,2));
        recyclerLiked.setAdapter(cuisineAdapter);
        cuisineAdapter.notifyDataSetChanged();
        loadingBar.dismiss();
    }
}