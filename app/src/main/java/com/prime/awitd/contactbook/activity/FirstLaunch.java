package com.prime.awitd.contactbook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prime.awitd.contactbook.R;
import com.prime.awitd.contactbook.model.ConnectivityReceiver;
import com.prime.awitd.contactbook.model.Member;
import com.prime.awitd.contactbook.model.MyApplication;
import com.prime.awitd.contactbook.setting.PrefManager;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by SantaClaus on 27/12/2016.
 */

public class FirstLaunch extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private DatabaseReference myRef;
    private Button btnUpload, btnSkip;
    private EditText editTextName, editTextID, editTextTeam, editTextPhone, editTextHome;
    String u_name, u_id, u_team, img_url;
    int u_phone, u_home;

    private PrefManager prefManager;
    private ProgressDialog progress;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public Uri downloadUrl;
    private ImageView imageView;
    ScrollView scrollView;

    private static final int SELECT_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(FirstLaunch.this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchMainActivity();
            finish();
        } else {
            startActivity(new Intent(this, GetStartActivity.class));
        }

        setContentView(R.layout.activity_upload);
        scrollView = (ScrollView) findViewById(R.id.sv);

        checkConnection();

        imageView = (ImageView) findViewById(R.id.imageButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });


        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (com.google.firebase.database.DatabaseException e) {
            e.printStackTrace();
        }

        //DataBase raf
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.keepSynced(true);


        btnUpload = (Button) findViewById(R.id.button_upload);
        btnSkip = (Button) findViewById(R.id.button_skip_upload);

        editTextName = (EditText) findViewById(R.id.edt_name);
        editTextID = (EditText) findViewById(R.id.edt_id);
        editTextTeam = (EditText) findViewById(R.id.edt_team);
        editTextPhone = (EditText) findViewById(R.id.edt_mobile);
        editTextHome = (EditText) findViewById(R.id.edt_home);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (editTextName.length() == 0 & editTextID.length() == 0 & editTextTeam.length() == 0 & editTextPhone.length() == 0 &
                        editTextHome.length() == 0) {
                    Snackbar.make(view, R.string.to_fill_all_textFill, Snackbar.LENGTH_LONG).show();
                } else if (editTextName.length() == 0) {
                    Snackbar.make(view, R.string.to_enter_name, Snackbar.LENGTH_SHORT).show();
                } else if (editTextID.length() == 0) {
                    Snackbar.make(view, R.string.to_enter_id, Snackbar.LENGTH_SHORT).show();
                } else if (editTextTeam.length() == 0) {
                    Snackbar.make(view, R.string.to_enter_team_name, Snackbar.LENGTH_SHORT).show();
                } else if (editTextPhone.length() == 0) {
                    Snackbar.make(view, R.string.to_enter_mobile_number, Snackbar.LENGTH_SHORT).show();
                } else if (editTextHome.length() == 0) {
                    Snackbar.make(view, R.string.to_enter_home_ph_number, Snackbar.LENGTH_SHORT).show();
                } else {

                    progress = new ProgressDialog(FirstLaunch.this);
                    progress.setMessage(getString(R.string.upload_progress_text));
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();

                    // Get the data from an ImageView as bytes
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    String path = "FileName/" + UUID.randomUUID() + ".png";

                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference(path);

                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Snackbar.make(view, R.string.upload_exception_text, Snackbar.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            downloadUrl = taskSnapshot.getDownloadUrl();
                            u_name = editTextName.getText().toString();
                            u_id = editTextID.getText().toString();
                            u_team = editTextTeam.getText().toString();
                            u_phone = Integer.parseInt(editTextPhone.getText().toString());
                            u_home = Integer.parseInt(editTextHome.getText().toString());
                            img_url = downloadUrl.toString();
                            final Member member = new Member(u_name, u_team, u_id, u_phone, u_home, img_url);
                            myRef.push().setValue(member);

                            editTextName.getText().clear();
                            editTextID.getText().clear();
                            editTextTeam.getText().clear();
                            editTextPhone.getText().clear();
                            editTextHome.getText().clear();

                            Toast.makeText(FirstLaunch.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                            Snackbar.make(view, "Succcessfully Uploaded! Congratulation! " + downloadUrl, Snackbar.LENGTH_SHORT).show();
                            progress.dismiss();
                            launchMainActivity();


                        }
                    });

                }
            }
        });

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = getString(R.string.connection_online);
            color = Color.WHITE;
        } else {
            message = getResources().getString(R.string.connection_offline);
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(scrollView, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_pic_from_gallary)), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                // get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i("First Launch", "Image Path : " + path);
                    // set the image in ImageView
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void launchMainActivity() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(FirstLaunch.this, MainFrameActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // to close onBackPressed ...
    }
}
