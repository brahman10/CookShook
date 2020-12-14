package com.example.cookshook.Fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cookshook.FavouriteCuisine;
import com.example.cookshook.FavouriteRecipe;
import com.example.cookshook.LoginActivity;
import com.example.cookshook.MainActivity;
import com.example.cookshook.Model.UserModel;
import com.example.cookshook.R;
import com.example.cookshook.Utils.LoadingBar;
import com.example.cookshook.Utils.SessionManagment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.cookshook.Utils.Constants.KEY_MOBILE;
import static com.example.cookshook.Utils.Constants.KEY_NAME;
import static com.example.cookshook.Utils.Constants.KEY_PROFILE_IMG;
import static com.example.cookshook.Utils.Constants.USER_REF;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    RelativeLayout llLogin ;
    LinearLayout llProfile , llhelpdesk , llfavCuisines , llfavRecipes;
    SessionManagment sessionManagment;
    LoadingBar loadingBar;
    CircleImageView imgUser;
    ImageView editImage;
    TextView txtname ,txtnumber;
    DatabaseReference userref;
    UserModel model;
    TextView logout;
    StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST =1;
    private Uri mImageUri;
    CardView login;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((MainActivity)getActivity()).setTitle("Profile");
        sessionManagment = new SessionManagment(getActivity());
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        llLogin = v.findViewById(R.id.llLogin);
        llfavCuisines = v.findViewById(R.id.favCuisines);
        llfavRecipes = v.findViewById(R.id.favRecipes);
        logout = v.findViewById(R.id.logout);
        llhelpdesk = v.findViewById(R.id.helpdesk);
        llProfile = v.findViewById(R.id.llprofile);
        imgUser = v.findViewById(R.id.imgUser);
        editImage = v.findViewById(R.id.edit_img);
        txtname = v.findViewById(R.id.txtname);
        txtnumber = v.findViewById(R.id.numbertxt);
        if (sessionManagment.isLogin())
        {
            llProfile.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE));
            loadingBar = new LoadingBar(getActivity());
            loadingBar.show();
            userref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    model = snapshot.getValue(UserModel.class);
                    if (sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)!=null)
                    {
                        Glide.with(getActivity()).load(sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)).into(imgUser);
                    }
                    loadingBar.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            });
            txtnumber.setText(sessionManagment.getUserDetails().get(KEY_MOBILE));
            txtname.setText(sessionManagment.getUserDetails().get(KEY_NAME));

            if (!(sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)==null || sessionManagment.getUserDetails().get(KEY_PROFILE_IMG).trim().equalsIgnoreCase("")))
            {
                Glide.with(getActivity()).load(sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)).into(imgUser);
            }
        }
        else
        {
            llProfile.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
        }


        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        sessionManagment.logoutSession();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
        llfavRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavouriteRecipe.class));
            }
        });
        llfavCuisines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavouriteCuisine.class));
            }
        });
        return v;
    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!= null && data.getData()!=null){
            mImageUri=data.getData();
            uploadFile();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        loadingBar.show();
        if(mImageUri!=null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(),"Uplaod Successfull",Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask= taskSnapshot.getStorage().getDownloadUrl();
                            while(!urlTask.isSuccessful());
                            Uri downloadUrl=urlTask.getResult();
                            model.setImg_url(downloadUrl.toString());
                            userref.setValue(model);
                            sessionManagment.updateProfile(model.getName(),model.getEmail(),downloadUrl.toString(),model.getStatus());
                            loadingBar.dismiss();
                            Glide.with(getActivity()).load(sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)).into(imgUser);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity() , e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){

                        }
                    });
        }
        else{
            Toast.makeText(getActivity(),"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
}