package com.example.cookshook.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cookshook.Adapter.CuisineAdapter;
import com.example.cookshook.AddRecipeActivity;
import com.example.cookshook.MainActivity;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.Model.SliderModel;
import com.example.cookshook.R;
import com.example.cookshook.RecipeActivity;
import com.example.cookshook.Utils.LoadingBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.cookshook.Utils.Constants.CUISINE_REF;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    FloatingActionButton fabAdd;
    List<String> cuisineName;
    List<String> cuisineImage;
    RecyclerView recyclerViewCuisine;
    CuisineAdapter cuisineAdapter;
    LoadingBar loadingBar;
    CarouselView carouselView;
    DatabaseReference sliderref , reciperef;
    List<SliderModel> sliders;
    List<RecipeModel> recipes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).setTitle("Cook Shook");
        loadingBar = new LoadingBar(getActivity());
        loadingBar.show();
        sliders = new ArrayList<>();
        recipes = new ArrayList<>();
        carouselView = v.findViewById(R.id.carousel);
        sliderref = FirebaseDatabase.getInstance().getReference().child("carousel");
        reciperef = FirebaseDatabase.getInstance().getReference().child(CUISINE_REF);
        sliderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()
                ) {
                    SliderModel model = s.getValue(SliderModel.class);
                    sliders.add(model);
                    reciperef.child(model.getCuisine()).child(model.getPostid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            RecipeModel model1 = snapshot1.getValue(RecipeModel.class);
                            recipes.add(model1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                carouselView.setImageListener(new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {
                        Glide.with(getActivity()).load(sliders.get(position).getImg()).into(imageView);
                    }
                });
                carouselView.setPageCount(sliders.size());
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
            }
        });
        cuisineName = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cuisines)));
        cuisineImage = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cuisines_image)));
        recyclerViewCuisine = v.findViewById(R.id.recyclerCuisine);
        recyclerViewCuisine.setLayoutManager(new GridLayoutManager(getActivity(),2));
        cuisineAdapter = new CuisineAdapter(cuisineImage,cuisineName,getActivity());
        recyclerViewCuisine.setAdapter(cuisineAdapter);
        cuisineAdapter.notifyDataSetChanged();
         fabAdd = v.findViewById(R.id.fabAdd);
         fabAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(), AddRecipeActivity.class));
             }
         });
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("dishmodel",recipes.get(position));
                startActivity(intent);
            }
        });
         return v;
    }
}