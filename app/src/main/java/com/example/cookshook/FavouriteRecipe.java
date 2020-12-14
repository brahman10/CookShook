package com.example.cookshook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cookshook.Adapter.CuisineAdapter;
import com.example.cookshook.Adapter.DishAdapter;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.Utils.LoadingBar;
import com.example.cookshook.Utils.RecipeLikeHandler;
import com.example.cookshook.Utils.SessionManagment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.cookshook.Utils.Constants.CUISINE_REF;
import static com.example.cookshook.Utils.Constants.KEY_ID;
import static com.example.cookshook.Utils.Constants.KEY_MOBILE;
import static com.example.cookshook.Utils.RecipeLikeHandler.COLUMN_ID;

public class FavouriteRecipe extends AppCompatActivity {

    RecipeLikeHandler recipeLikeHandler;
    SessionManagment sessionManagment;
    ArrayList<HashMap<String,String>> likedRecipe;
    List<RecipeModel> favRecipes;
    RecyclerView recyclerLiked;
    DishAdapter dishAdapter;
    DatabaseReference reciperef;
    LoadingBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_recipe);
        recipeLikeHandler = new RecipeLikeHandler(this);
        sessionManagment = new SessionManagment(this);
        loadingBar = new LoadingBar(this);
        likedRecipe = new ArrayList<>();
        favRecipes = new ArrayList<>();
        recyclerLiked = findViewById(R.id.rec_like);
        Log.e("id",sessionManagment.getUserDetails().get(KEY_ID));
        likedRecipe = recipeLikeHandler.getWishtableAll(sessionManagment.getUserDetails().get(KEY_ID));
        reciperef = FirebaseDatabase.getInstance().getReference().child(CUISINE_REF);
        dishAdapter = new DishAdapter(favRecipes,this);
        recyclerLiked.setLayoutManager(new GridLayoutManager(this,2));
        recyclerLiked.setAdapter(dishAdapter);
        dishAdapter.notifyDataSetChanged();
        Log.e("  d",likedRecipe.toString());
        for (HashMap<String,String > hashMap:likedRecipe) {
            Log.e( "onCreate: ",hashMap.toString() );
            String postid = hashMap.get(COLUMN_ID);
            reciperef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loadingBar.show();
                    favRecipes.clear();
                    for (DataSnapshot snap:snapshot.getChildren()
                         ) {
                        for (DataSnapshot s:snap.getChildren()
                             ) {
                            RecipeModel recipeModel = s.getValue(RecipeModel.class);
                            if (recipeModel.getId().equalsIgnoreCase(postid))
                            {
                                favRecipes.add(recipeModel);
                            }
                        }
                    }
                    dishAdapter.notifyDataSetChanged();
                    loadingBar.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            });

        }

    }
}