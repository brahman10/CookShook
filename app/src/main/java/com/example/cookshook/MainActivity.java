package com.example.cookshook;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.cookshook.Fragments.DashboardFragment;
import com.example.cookshook.Fragments.HomeFragment;
import com.example.cookshook.Fragments.SearchFragment;
import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;

public class MainActivity extends AppCompatActivity {

    BubbleTabBar bubbleTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubbleTabBar=findViewById(R.id.bubbleTabBar);
        if(savedInstanceState==null)
        {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("home").commit();

        }
        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                if (i==R.id.home)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("home").commit();
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                }
                else if (i==R.id.search)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new SearchFragment()).addToBackStack("search").commit();
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
                }
                else
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new DashboardFragment()).addToBackStack("dash").commit();
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_200)));
                }
            }
        });
    }
}