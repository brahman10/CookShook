package com.example.cookshook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookshook.Adapter.DishAdapter;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.Utils.LoadingBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.cookshook.Utils.Constants.CUISINE_REF;

public class DishlistActivity extends AppCompatActivity {

    String cuisine;
    DatabaseReference cuisineref;
    LoadingBar loadingBar;
    List<RecipeModel> dishlist;
    DishAdapter dishAdapter;
    TextView txtTitle;
    RecyclerView recyclerViewDish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishlist);
        loadingBar = new LoadingBar(this);
        txtTitle = findViewById(R.id.txtTitle);
        cuisine = getIntent().getStringExtra("cuisine");
        txtTitle.setText(cuisine);
        dishlist = new ArrayList<>();
        recyclerViewDish = findViewById(R.id.recyclerDish);
        recyclerViewDish.setLayoutManager(new GridLayoutManager(this,2));
        dishAdapter = new DishAdapter(dishlist,this);
        recyclerViewDish.setAdapter(dishAdapter);
        cuisineref= FirebaseDatabase.getInstance().getReference().child(CUISINE_REF).child(cuisine);
        cuisineref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Snap1",snapshot.toString());
                loadingBar.show();
                dishlist.clear();
                if (snapshot.hasChildren())
                {
                    for (DataSnapshot snap :snapshot.getChildren()
                    ) {
                        Log.e("Snap2",snap.toString());
                        RecipeModel model = snap.getValue(RecipeModel.class);
                        dishlist.add(model);
                    }
                }
                else
                {
                    Toast.makeText(DishlistActivity.this,"No Dish Available",Toast.LENGTH_LONG).show();
                }
                dishAdapter.notifyDataSetChanged();
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DishlistActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}