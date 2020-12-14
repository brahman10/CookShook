package com.example.cookshook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.opengl.ETC1;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cookshook.Model.RecipeModel;
import com.example.cookshook.Utils.LoadingBar;
import com.example.cookshook.Utils.SessionManagment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.cookshook.Utils.Constants.CUISINE_REF;
import static com.example.cookshook.Utils.Constants.KEY_MOBILE;
import static com.example.cookshook.Utils.Constants.USER_REF;

public class AddRecipeActivity extends AppCompatActivity {

    String cuisine , recipename , duration , recipe ;
    SessionManagment sessionManagment;
    EditText etRecipeName , etDuration , etRecipe;
    LoadingBar loadingBar;
    ImageView imgRecipe;
    Spinner spinnerCuisine;
    CardView uploadImage;
    TextView btnPost;
    private static final int PICK_IMAGE_REQUEST =1;
    private Uri mImageUri;
    StorageReference mStorageRef;
    DatabaseReference cuisineref , userref;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        initViews();
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisine = spinnerCuisine.getSelectedItem().toString();
                recipename = etRecipeName.getText().toString();
                recipe = etRecipe.getText().toString();
                duration = etDuration.getText().toString();
                if (TextUtils.isEmpty(recipename))
                {
                    etRecipeName.setError("Enter Recipe Name");
                    etRecipeName.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(duration))
                {
                    etDuration.setError("Enter Duration");
                    etDuration.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(recipe))
                {
                    etRecipe.setError("Enter Recipe");
                    etRecipe.requestFocus();
                    return;
                }
                else if (mImageUri==null)
                {
                    Toast.makeText(getApplicationContext(),"Select Image" ,Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    uploadFile();
                }
            }
        });
    }

    private void initViews() {
        etRecipeName = findViewById(R.id.etName);
        etDuration = findViewById(R.id.etDuration);
        etRecipe = findViewById(R.id.etRecipe);
        spinnerCuisine = findViewById(R.id.spinnerCuisine);
        imgRecipe = findViewById(R.id.imgRecipe);
        uploadImage = findViewById(R.id.uploadImage);
        btnPost = findViewById(R.id.btnPost);
        sessionManagment = new SessionManagment(this);
        loadingBar = new LoadingBar(this);
        cuisine= "Chinese";
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE)).child(CUISINE_REF);
        cuisineref = FirebaseDatabase.getInstance().getReference().child(CUISINE_REF);
    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!= null && data.getData()!=null){
            mImageUri=data.getData();
            imgRecipe.setVisibility(View.VISIBLE);
            Glide.with(this).load(mImageUri).into(imgRecipe);
        }
    }
    private void uploadFile(){
        loadingBar.show();
        if(mImageUri!=null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask= taskSnapshot.getStorage().getDownloadUrl();
                            while(!urlTask.isSuccessful());
                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            Uri downloadUrl=urlTask.getResult();
                            String postId = cuisineref.push().getKey();
                            RecipeModel model = new RecipeModel(postId,cuisine,recipename,recipe,downloadUrl.toString(),duration,currentDate,currentTime);
                            cuisineref.child(cuisine).child(postId).setValue(model);
                            userref.child(cuisine).child(postId).setValue(model);
                            Toast.makeText(AddRecipeActivity.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext() , e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){

                        }
                    });
        }
        else
        {
            loadingBar.dismiss();
            Toast.makeText(getApplicationContext(),"Select Image",Toast.LENGTH_SHORT).show();
        }
    }
}