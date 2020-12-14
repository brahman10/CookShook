package com.example.cookshook.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cookshook.Adapter.SuggestAdapter;
import com.example.cookshook.MainActivity;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.R;
import com.example.cookshook.Utils.LoadingBar;
import com.example.cookshook.Utils.SessionManagment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.cookshook.Utils.Constants.CUISINE_REF;


public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    EditText search;
    DatabaseReference cuisineref;
    List<RecipeModel> users,suggestions;
    RecyclerView listView;
    SuggestAdapter suggestAdapter;
    LoadingBar loadingBar ;
    SessionManagment sessionManagment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ((MainActivity)getActivity()).setTitle("Search");
        cuisineref = FirebaseDatabase.getInstance().getReference().child(CUISINE_REF);
        loadingBar = new LoadingBar(getActivity());
        sessionManagment = new SessionManagment(getActivity());
        loadingBar.show();
        getData();
        users = new ArrayList<>();
        listView = v.findViewById(R.id.listSuggest);
        suggestions = new ArrayList<>();
        suggestAdapter = new SuggestAdapter(suggestions,getActivity());
        search = v.findViewById(R.id.etsearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = String.valueOf(s);
                if (str.length()>1){
                    findsuggestion(s);
                    listView.setVisibility(View.VISIBLE);
                }
                else
                {
                    suggestions.clear();
                    listView.setVisibility(View.GONE);
                    suggestAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                findsuggestion(s);
            }
        });
        return v;
    }

    private void findsuggestion(CharSequence s) {
        suggestions.clear();
        String str = String.valueOf(s);
        for (RecipeModel m:users) {
            if (m.getRecipename().toLowerCase().contains(str.toLowerCase()))
            {
                suggestions.add(m);
            }
        }
        listView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        listView.setAdapter(suggestAdapter);
        suggestAdapter.notifyDataSetChanged();

    }

    private void getData() {
        cuisineref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()
                     ) {
                    for (DataSnapshot ds:s.getChildren()) {
                        RecipeModel model = ds.getValue(RecipeModel.class);
                        users.add(model);
                    }
                }
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}