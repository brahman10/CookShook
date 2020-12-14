package com.example.cookshook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cookshook.Model.UserModel;
import com.example.cookshook.Utils.LoadingBar;
import com.example.cookshook.Utils.SessionManagment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.cookshook.Utils.Constants.USER_REF;

public class LoginActivity extends AppCompatActivity {
    CardView login;
    TextView register , forgotPass , skip;
    EditText etMobile,etPassword;
    String mobile , pass;
    DatabaseReference userref;
    LoadingBar loadingBar;
    SessionManagment sessionManagment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        skip = findViewById(R.id.skip);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF);
        sessionManagment = new SessionManagment(LoginActivity.this);
        loadingBar = new LoadingBar(LoginActivity.this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = etMobile.getText().toString();
                pass = etPassword.getText().toString();
                if (TextUtils.isEmpty(mobile))
                {
                    etMobile.setError("Enter Mobile");
                    etMobile.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pass))
                {
                    etPassword.setError("Enter Password");
                    etPassword.requestFocus();
                    return;
                }
                else
                {
                    loadingBar.show();
                    userLogin(mobile,pass);
                }

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("type","r");
                startActivity(intent);
            }
        });
        forgotPass = findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("type","f");
                startActivity(intent);
            }
        });
    }

    private void userLogin(String mobile, String pass) {
        String number = "+91"+mobile;
        userref.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0)
                {
                    Log.e("Data",dataSnapshot.toString());
                    UserModel model=dataSnapshot.getValue(UserModel.class);
                    if(pass.equals(model.getPassword().toString()))
                    {
                        loadingBar.dismiss();
                        sessionManagment.createLoginSession(model.getId(),model.getName(),model.getMobile(),model.getImg_url(),model.getEmail(),model.getStatus(),null,null,null);
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        loadingBar.dismiss();
                        etPassword.setError("Invalid Password");
                        etPassword.requestFocus();
                        return;
                    }

                }

                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Mobile number not registered",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (sessionManagment.isLogin())
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}