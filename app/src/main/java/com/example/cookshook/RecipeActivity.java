package com.example.cookshook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.ETC1;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cookshook.Model.ChatMessage;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.Utils.SessionManagment;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.cookshook.Utils.Constants.COMMENT_REF;
import static com.example.cookshook.Utils.Constants.CUISINE_REF;
import static com.example.cookshook.Utils.Constants.KEY_NAME;

public class RecipeActivity extends AppCompatActivity {

    RecipeModel recipeModel;
    TextView txtTitle , txtDishName,txtDuration ,txtRecipe;
    ListView list_of_comments;
    EditText input;
    FloatingActionButton fab;
    ImageView imgDish;
    RelativeLayout activity_comment;
    DatabaseReference commentref;
    SessionManagment sessionManagment;
    int numofMess=0;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        sessionManagment = new SessionManagment(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeModel = (RecipeModel) getIntent().getSerializableExtra("dishmodel");
        }
        commentref = FirebaseDatabase.getInstance().getReference().child(CUISINE_REF).child(recipeModel.getCuisine()).child(recipeModel.getId()).child(COMMENT_REF);
        txtTitle = findViewById(R.id.txtTitle);
        activity_comment = findViewById(R.id.activity_comment);
        if (!sessionManagment.isLogin())
        {
            activity_comment.setVisibility(View.GONE);
        }
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);
        list_of_comments = (ListView)findViewById(R.id.list_of_comments);
        txtDishName = findViewById(R.id.txtDishName);
        txtDuration = findViewById(R.id.txtDuration);
        txtRecipe = findViewById(R.id.txtRecipe);
        imgDish = findViewById(R.id.imgDish);
        txtTitle.setText(recipeModel.getRecipename());
        txtDishName.setText(recipeModel.getRecipename());
        txtRecipe . setText(recipeModel.getRecipe());
        txtDuration.setText("Duration: "+recipeModel.getDuration()+" min");
        Glide.with(this).load(recipeModel.getImgurl()).into(imgDish);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                String mes = input.getText().toString();
                if (!(mes==null || mes.equals("")))
                {
                    commentref.push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    sessionManagment.getUserDetails().get(KEY_NAME)));

                    input.setText("");
                    displayChatMessages();
                }

            }
        });
    }
    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, commentref) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));

            }
        };


        list_of_comments.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        displayChatMessages();
        getList();
    }

    private void getList() {
        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numofMess=0;
                if (snapshot.hasChildren())
                {
                    for (DataSnapshot snap:snapshot.getChildren()
                         ) {
                        numofMess++;
                    }
                    ViewGroup.LayoutParams params = list_of_comments.getLayoutParams();
                    params.height = 110*numofMess;
                    list_of_comments.setLayoutParams(params);
                    list_of_comments.requestLayout();
                    list_of_comments.setVisibility(View.VISIBLE);
                }
                else {
                    list_of_comments.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}